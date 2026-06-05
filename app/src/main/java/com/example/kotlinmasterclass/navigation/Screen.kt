package com.example.kotlinmasterclass.navigation

sealed class Screen(val route: String) {
    object Settings : Screen("settings")
    object Dashboard : Screen("dashboard")
    object Coroutines : Screen("coroutines_tutorial")
    object ScopeFunctions : Screen("scope_functions_tutorial")
    object ExtensionFunctions : Screen("extension_functions_tutorial")
    object HigherOrderFunctions : Screen("higher_order_functions_tutorial")
    object SealedClasses : Screen("sealed_classes_tutorial")
    object Generics : Screen("generics_variance_tutorial")
    object KotlinFlow : Screen("flow_tutorial")
    object AdvancedConcurrency : Screen("advanced_concurrency_tutorial")
    object Delegation : Screen("delegation_tutorial")
    object Performance : Screen("performance_tutorial")
    object Contracts : Screen("contracts_tutorial")
    object ContextReceivers : Screen("context_receivers_tutorial")
    object CanvasAnimation : Screen("canvas_animation_tutorial")
    object MotionLayout : Screen("motion_layout_tutorial")
    object MusicPlayer : Screen("music_player_tutorial")
    object Testing : Screen("testing_tutorial")
    object GlassWallet : Screen("glass_wallet_tutorial")
    // We will add more routes here as we build out the app
}