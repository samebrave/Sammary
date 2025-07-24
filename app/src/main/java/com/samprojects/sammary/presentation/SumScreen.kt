package com.samprojects.sammary.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.samprojects.sammary.model.SummarizerViewModel

@Composable
fun SumScreen(navController: NavController, viewModel: SummarizerViewModel) {
    val title by viewModel.videoTitle
    val transcript by viewModel.transcript
    val isLoading by viewModel.isLoading
    val error by viewModel.error
    val preferences by viewModel.userPreferences

    Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.Start
    ) {
        when {
            isLoading -> {
                Text(text = "Loading...", fontSize = 18.sp)
            }
            error != null -> {
                Text(text = error ?: "Unknown error", color = Color.Red)
            }
            else -> {
                Text(
                        text = title,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 26.sp,
                        color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                        text = transcript,
                        fontSize = preferences.summaryFontSize.sp,
                        lineHeight = (preferences.summaryFontSize * 1.5f).sp,
                        color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}
