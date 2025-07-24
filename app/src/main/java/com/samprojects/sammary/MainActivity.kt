package com.samprojects.sammary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.samprojects.sammary.model.SummarizerViewModel
import com.samprojects.sammary.model.SummarizerViewModelFactory
import com.samprojects.sammary.navigation.MainScreen
import com.samprojects.sammary.ui.theme.SammaryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MyApp() }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val viewModel: SummarizerViewModel =
            viewModel(
                    factory =
                            SummarizerViewModelFactory(
                                    context.applicationContext as android.app.Application
                            )
            )
    val preferences by viewModel.userPreferences

    SammaryTheme(darkTheme = preferences.isDarkMode) {
        MainScreen(navController = navController, viewModel = viewModel)
    }
}
