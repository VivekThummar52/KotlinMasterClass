package com.example.kotlinmasterclass.navigation

sealed class Screen(val route: String) {
    object Settings : Screen("settings")
    object Dashboard : Screen("dashboard")
    object Coroutines : Screen("coroutines_tutorial")
    object ScopeFunctions : Screen("scope_functions_tutorial")
    // We will add more routes here as we build out the app
}