package com.samprojects.sammary.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.samprojects.sammary.model.SummarizerViewModel
import com.samprojects.sammary.navigationbar.CrNavigationBar
import com.samprojects.sammary.presentation.HistoryScreen
import com.samprojects.sammary.presentation.HomeScreen
import com.samprojects.sammary.presentation.SettingsScreen
import com.samprojects.sammary.presentation.SumScreen

@Composable
fun MainScreen(navController: NavHostController, viewModel: SummarizerViewModel) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route ?: "home"

    Scaffold(
            bottomBar = {
                CrNavigationBar(
                        selectedItem = currentRoute,
                        onItemSelected = { route ->
                            if (route != currentRoute) {
                                navController.navigate(route) {
                                    popUpTo("home") { inclusive = false }
                                    launchSingleTop = true
                                }
                            }
                        }
                )
            }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(navController = navController, startDestination = "home") {
                composable("home") {
                    HomeScreen(navController = navController, viewModel = viewModel)
                }
                composable("history") {
                    HistoryScreen(navController = navController, viewModel = viewModel)
                }
                composable("settings") {
                    SettingsScreen(navController = navController, viewModel = viewModel)
                }
                composable("summary") {
                    SumScreen(navController = navController, viewModel = viewModel)
                }
            }
        }
    }
}
