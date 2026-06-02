package com.example.kotlinmasterclass.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kotlinmasterclass.features.dashboard.DashboardScreen
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kotlinmasterclass.features.coroutines.CoroutinesScreen
import com.example.kotlinmasterclass.features.scopefunctions.ScopeFunctionsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Dashboard.route) {

        composable(route = Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToCoroutines = { navController.navigate(Screen.Coroutines.route) },
                onNavigateToScopeFunctions = { navController.navigate(Screen.ScopeFunctions.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
            )
        }

        // Placeholder for Coroutines Screen (We will build this in the next phase)
        composable(route = Screen.Coroutines.route) {
            val coroutinesViewModel: com.example.kotlinmasterclass.features.coroutines.CoroutinesViewModel = hiltViewModel()
            CoroutinesScreen(
                viewModel = coroutinesViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        // Placeholder for Scope Functions Screen
        composable(route = Screen.ScopeFunctions.route) {
            val scopeFunctionsViewModel: com.example.kotlinmasterclass.features.scopefunctions.ScopeFunctionsViewModel = hiltViewModel()
            ScopeFunctionsScreen(
                viewModel = scopeFunctionsViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = Screen.Settings.route) {
            // We use the activity scope here so the theme changes apply globally
            val settingsViewModel: com.example.kotlinmasterclass.features.settings.SettingsViewModel =
                androidx.hilt.navigation.compose.hiltViewModel(androidx.compose.ui.platform.LocalContext.current as androidx.activity.ComponentActivity)

            com.example.kotlinmasterclass.features.settings.SettingsScreen(
                viewModel = settingsViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}