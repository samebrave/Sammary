package com.samprojects.sammary.model

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.samprojects.sammary.api.RetrofitClient
import com.samprojects.sammary.database.SummaryDatabase
import com.samprojects.sammary.repository.SummaryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

data class SummaryItem(val id: Long = 0, val title: String, val transcript: String)

data class UserPreferences(
        val isDarkMode: Boolean = false,
        val summaryFontSize: Float = 16f,
        val autoSave: Boolean = true,
        val language: String = "en"
)

class SummarizerViewModel(application: Application) : AndroidViewModel(application) {
    var videoTitle = mutableStateOf("")
    var transcript = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var error = mutableStateOf<String?>(null)

    var userPreferences = mutableStateOf(UserPreferences())
        private set

    private val repository: SummaryRepository

    var history = mutableStateListOf<SummaryItem>()
        private set

    init {
        val database = SummaryDatabase.getDatabase(application)
        repository = SummaryRepository(database.summaryDao())
        loadSummariesFromDatabase()
    }

    private fun loadSummariesFromDatabase() {
        viewModelScope.launch {
            repository.getAllSummaries().collect { summaries ->
                history.clear()
                history.addAll(summaries)
            }
        }
    }

    fun fetchTranscript(videoUrl: String, language: String = "en") {
        val videoId = extractYoutubeId(videoUrl)
        if (videoId == null) {
            error.value = "Invalid YouTube URL"
            return
        }

        isLoading.value = true
        error.value = null

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                        RetrofitClient.api
                                .getSummary(videoId = videoId, language = language)
                                .execute()

                if (response.isSuccessful) {
                    val json = JSONObject(response.body()?.string() ?: "")
                    val summary = json.optString("translated_transcript", "Sum unavailable")
                    val generatedTitle = generateTitleFromSummary(summary)
                    val summaryItem = SummaryItem(title = generatedTitle, transcript = summary)

                    withContext(Dispatchers.Main) {
                        videoTitle.value = summaryItem.title
                        transcript.value = summaryItem.transcript
                        isLoading.value = false
                    }

                    // Save to database
                    repository.insertSummary(summaryItem)
                } else {
                    withContext(Dispatchers.Main) {
                        error.value = "Error: ${response.code()}"
                        isLoading.value = false
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    error.value = "Error: ${e.localizedMessage}"
                    isLoading.value = false
                }
            }
        }
    }

    fun deleteSummary(summaryItem: SummaryItem) {
        viewModelScope.launch {
            if (summaryItem.id != 0L) {
                repository.deleteSummary(summaryItem.id)
            }
        }
    }

    fun deleteAllSummaries() {
        viewModelScope.launch { repository.deleteAllSummaries() }
    }

    private fun generateTitleFromSummary(summary: String): String {
        val words =
                summary.trim()
                        .split(Regex("\\s+"))
                        .filter { it.isNotBlank() && it.length > 2 }
                        .take(3)

        return if (words.isNotEmpty()) {
            words.joinToString(" ").take(30) + if (summary.length > 30) "..." else ""
        } else {
            "Summary ${System.currentTimeMillis() % 1000}"
        }
    }

    fun updateDarkMode(isDarkMode: Boolean) {
        userPreferences.value = userPreferences.value.copy(isDarkMode = isDarkMode)
    }

    fun updateFontSize(fontSize: Float) {
        userPreferences.value = userPreferences.value.copy(summaryFontSize = fontSize)
    }

    fun updateAutoSave(enabled: Boolean) {
        userPreferences.value = userPreferences.value.copy(autoSave = enabled)
    }

    fun updateLanguage(language: String) {
        userPreferences.value = userPreferences.value.copy(language = language)
    }
}

fun extractYoutubeId(url: String): String? {
    val regex = "(?<=v=|/videos/|embed/|youtu.be/)[^#&?\\n]*".toRegex()
    return regex.find(url)?.value
}
