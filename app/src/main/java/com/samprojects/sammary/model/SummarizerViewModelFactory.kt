package com.samprojects.sammary.model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SummarizerViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SummarizerViewModel::class.java)) {
            return SummarizerViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
