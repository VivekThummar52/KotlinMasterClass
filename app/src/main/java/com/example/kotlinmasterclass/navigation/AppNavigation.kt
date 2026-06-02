package com.example.kotlinmasterclass.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kotlinmasterclass.features.coroutines.CoroutinesScreen
import com.example.kotlinmasterclass.features.coroutines.CoroutinesViewModel
import com.example.kotlinmasterclass.features.dashboard.DashboardScreen
import com.example.kotlinmasterclass.features.extensionfunctions.ExtensionFunctionsScreen
import com.example.kotlinmasterclass.features.extensionfunctions.ExtensionFunctionsViewModel
import com.example.kotlinmasterclass.features.generics.GenericsScreen
import com.example.kotlinmasterclass.features.generics.GenericsViewModel
import com.example.kotlinmasterclass.features.higherorderfunctions.HigherOrderFunctionsScreen
import com.example.kotlinmasterclass.features.higherorderfunctions.HigherOrderFunctionsViewModel
import com.example.kotlinmasterclass.features.scopefunctions.ScopeFunctionsScreen
import com.example.kotlinmasterclass.features.scopefunctions.ScopeFunctionsViewModel
import com.example.kotlinmasterclass.features.sealedclasses.SealedClassesScreen
import com.example.kotlinmasterclass.features.sealedclasses.SealedClassesViewModel
import com.example.kotlinmasterclass.features.settings.SettingsScreen
import com.example.kotlinmasterclass.features.settings.SettingsViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Dashboard.route) {

        composable(route = Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToCoroutines = { navController.navigate(Screen.Coroutines.route) },
                onNavigateToScopeFunctions = { navController.navigate(Screen.ScopeFunctions.route) },
                onNavigateToExtensionFunctions = { navController.navigate(Screen.ExtensionFunctions.route) },
                onNavigateToHigherOrderFunctions = { navController.navigate(Screen.HigherOrderFunctions.route) },
                onNavigateToSealedClasses = { navController.navigate(Screen.SealedClasses.route) },
                onNavigateToGenerics = { navController.navigate(Screen.Generics.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) })
        }

        // Placeholder for Coroutines Screen (We will build this in the next phase)
        composable(route = Screen.Coroutines.route) {
            val coroutinesViewModel: CoroutinesViewModel = hiltViewModel()
            CoroutinesScreen(
                viewModel = coroutinesViewModel, onBackClick = { navController.popBackStack() })
        }

        // Placeholder for Scope Functions Screen
        composable(route = Screen.ScopeFunctions.route) {
            val scopeFunctionsViewModel: ScopeFunctionsViewModel = hiltViewModel()
            ScopeFunctionsScreen(
                viewModel = scopeFunctionsViewModel, onBackClick = { navController.popBackStack() })
        }

        composable(route = Screen.Settings.route) {
            // We use the activity scope here so the theme changes apply globally
            val settingsViewModel: SettingsViewModel =
                hiltViewModel(androidx.compose.ui.platform.LocalContext.current as androidx.activity.ComponentActivity)
            SettingsScreen(
                viewModel = settingsViewModel, onBackClick = { navController.popBackStack() })
        }

        composable(route = Screen.ExtensionFunctions.route) {
            val extensionViewModel: ExtensionFunctionsViewModel = hiltViewModel()
            ExtensionFunctionsScreen(
                viewModel = extensionViewModel, onBackClick = { navController.popBackStack() })
        }

        composable(route = Screen.HigherOrderFunctions.route) {
            val higherOrderViewModel: HigherOrderFunctionsViewModel = hiltViewModel()
            HigherOrderFunctionsScreen(
                viewModel = higherOrderViewModel, onBackClick = { navController.popBackStack() })
        }

        composable(route = Screen.SealedClasses.route) {
            val sealedViewModel: SealedClassesViewModel = hiltViewModel()
            SealedClassesScreen(
                viewModel = sealedViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = Screen.Generics.route) {
            val genericsViewModel: GenericsViewModel = hiltViewModel()
            GenericsScreen(
                viewModel = genericsViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}