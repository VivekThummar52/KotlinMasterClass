package com.example.kotlinmasterclass.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kotlinmasterclass.features.canvas.CanvasScreen
import com.example.kotlinmasterclass.features.canvas.CanvasViewModel
import com.example.kotlinmasterclass.features.concurrency.AdvancedConcurrencyScreen
import com.example.kotlinmasterclass.features.concurrency.AdvancedConcurrencyViewModel
import com.example.kotlinmasterclass.features.contextreceivers.ContextReceiversScreen
import com.example.kotlinmasterclass.features.contextreceivers.ContextReceiversViewModel
import com.example.kotlinmasterclass.features.contracts.ContractsScreen
import com.example.kotlinmasterclass.features.contracts.ContractsViewModel
import com.example.kotlinmasterclass.features.coroutines.CoroutinesScreen
import com.example.kotlinmasterclass.features.coroutines.CoroutinesViewModel
import com.example.kotlinmasterclass.features.dashboard.DashboardScreen
import com.example.kotlinmasterclass.features.delegation.DelegationScreen
import com.example.kotlinmasterclass.features.delegation.DelegationViewModel
import com.example.kotlinmasterclass.features.extensionfunctions.ExtensionFunctionsScreen
import com.example.kotlinmasterclass.features.extensionfunctions.ExtensionFunctionsViewModel
import com.example.kotlinmasterclass.features.flow.FlowScreen
import com.example.kotlinmasterclass.features.flow.FlowViewModel
import com.example.kotlinmasterclass.features.generics.GenericsScreen
import com.example.kotlinmasterclass.features.generics.GenericsViewModel
import com.example.kotlinmasterclass.features.higherorderfunctions.HigherOrderFunctionsScreen
import com.example.kotlinmasterclass.features.higherorderfunctions.HigherOrderFunctionsViewModel
import com.example.kotlinmasterclass.features.morph.MorphScreen
import com.example.kotlinmasterclass.features.motion.MotionScreen
import com.example.kotlinmasterclass.features.motion.MotionViewModel
import com.example.kotlinmasterclass.features.musicplayer.MusicPlayerScreen
import com.example.kotlinmasterclass.features.musicplayer.MusicPlayerViewModel
import com.example.kotlinmasterclass.features.missioncontrol.MissionControlScreen
import com.example.kotlinmasterclass.features.missioncontrol.MissionControlViewModel
import com.example.kotlinmasterclass.features.performance.PerformanceScreen
import com.example.kotlinmasterclass.features.performance.PerformanceViewModel
import com.example.kotlinmasterclass.features.scopefunctions.ScopeFunctionsScreen
import com.example.kotlinmasterclass.features.scopefunctions.ScopeFunctionsViewModel
import com.example.kotlinmasterclass.features.sealedclasses.SealedClassesScreen
import com.example.kotlinmasterclass.features.sealedclasses.SealedClassesViewModel
import com.example.kotlinmasterclass.features.settings.SettingsScreen
import com.example.kotlinmasterclass.features.settings.SettingsViewModel
import com.example.kotlinmasterclass.features.testing.TestingScreen
import com.example.kotlinmasterclass.features.testing.TestingViewModel
import com.example.kotlinmasterclass.features.wallet.GlassWalletScreen
import com.example.kotlinmasterclass.features.wallet.WalletViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navigateToSettings = {
        navController.navigate(Screen.Settings.route) {
            launchSingleTop = true
        }
    }

    NavHost(navController = navController, startDestination = Screen.Dashboard.route) {

        composable(route = Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToCoroutines = { navController.navigate(Screen.Coroutines.route) },
                onNavigateToScopeFunctions = { navController.navigate(Screen.ScopeFunctions.route) },
                onNavigateToExtensionFunctions = { navController.navigate(Screen.ExtensionFunctions.route) },
                onNavigateToHigherOrderFunctions = { navController.navigate(Screen.HigherOrderFunctions.route) },
                onNavigateToSealedClasses = { navController.navigate(Screen.SealedClasses.route) },
                onNavigateToGenerics = { navController.navigate(Screen.Generics.route) },
                onNavigateToFlow = { navController.navigate(Screen.KotlinFlow.route) },
                onNavigateToConcurrency = { navController.navigate(Screen.AdvancedConcurrency.route) },
                onNavigateToDelegation = { navController.navigate(Screen.Delegation.route) },
                onNavigateToPerformance = { navController.navigate(Screen.Performance.route) },
                onNavigateToContracts = { navController.navigate(Screen.Contracts.route) },
                onNavigateToContextReceivers = { navController.navigate(Screen.ContextReceivers.route) },
                onNavigateToCanvas = { navController.navigate(Screen.CanvasAnimation.route) },
                onNavigateToMotion = { navController.navigate(Screen.MotionLayout.route) },
                onNavigateToMusicPlayer = { navController.navigate(Screen.MusicPlayer.route) },
                onNavigateToTesting = { navController.navigate(Screen.Testing.route) },
                onNavigateToGlassWallet = { navController.navigate(Screen.GlassWallet.route) },
                onNavigateToMorph = { navController.navigate(Screen.MorphLayout.route) },
                onNavigateToMissionControl = { navController.navigate(Screen.MissionControl.route) },
                onNavigateToSettings = navigateToSettings)
        }

        composable(route = Screen.MissionControl.route) {
            val missionControlViewModel: MissionControlViewModel = hiltViewModel()
            MissionControlScreen(
                viewModel = missionControlViewModel,
                onBackClick = { navController.popBackStack() },
                onSettingsClick = navigateToSettings
            )
        }

        // Placeholder for Coroutines Screen (We will build this in the next phase)
        composable(route = Screen.Coroutines.route) {
            val coroutinesViewModel: CoroutinesViewModel = hiltViewModel()
            CoroutinesScreen(
                viewModel = coroutinesViewModel,
                onBackClick = { navController.popBackStack() },
                onSettingsClick = navigateToSettings
            )
        }

        // Placeholder for Scope Functions Screen
        composable(route = Screen.ScopeFunctions.route) {
            val scopeFunctionsViewModel: ScopeFunctionsViewModel = hiltViewModel()
            ScopeFunctionsScreen(
                viewModel = scopeFunctionsViewModel,
                onBackClick = { navController.popBackStack() },
                onSettingsClick = navigateToSettings
            )
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
                viewModel = extensionViewModel,
                onBackClick = { navController.popBackStack() },
                onSettingsClick = navigateToSettings
            )
        }

        composable(route = Screen.HigherOrderFunctions.route) {
            val higherOrderViewModel: HigherOrderFunctionsViewModel = hiltViewModel()
            HigherOrderFunctionsScreen(
                viewModel = higherOrderViewModel,
                onBackClick = { navController.popBackStack() },
                onSettingsClick = navigateToSettings
            )
        }

        composable(route = Screen.SealedClasses.route) {
            val sealedViewModel: SealedClassesViewModel = hiltViewModel()
            SealedClassesScreen(
                viewModel = sealedViewModel,
                onBackClick = { navController.popBackStack() },
                onSettingsClick = navigateToSettings
            )
        }

        composable(route = Screen.Generics.route) {
            val genericsViewModel: GenericsViewModel = hiltViewModel()
            GenericsScreen(
                viewModel = genericsViewModel,
                onBackClick = { navController.popBackStack() },
                onSettingsClick = navigateToSettings
            )
        }

        composable(route = Screen.KotlinFlow.route) {
            val flowViewModel: FlowViewModel = hiltViewModel()
            FlowScreen(
                viewModel = flowViewModel,
                onBackClick = { navController.popBackStack() },
                onSettingsClick = navigateToSettings
            )
        }

        composable(route = Screen.AdvancedConcurrency.route) {
            val concurrencyViewModel: AdvancedConcurrencyViewModel = hiltViewModel()
            AdvancedConcurrencyScreen(
                viewModel = concurrencyViewModel,
                onBackClick = { navController.popBackStack() },
                onSettingsClick = navigateToSettings
            )
        }

        composable(route = Screen.Delegation.route) {
            val delegationViewModel: DelegationViewModel = hiltViewModel()
            DelegationScreen(
                viewModel = delegationViewModel,
                onBackClick = { navController.popBackStack() },
                onSettingsClick = navigateToSettings
            )
        }

        composable(route = Screen.Performance.route) {
            val performanceViewModel: PerformanceViewModel = hiltViewModel()
            PerformanceScreen(
                viewModel = performanceViewModel,
                onBackClick = { navController.popBackStack() },
                onSettingsClick = navigateToSettings
            )
        }

        composable(route = Screen.Contracts.route) {
            val contractsViewModel: ContractsViewModel = hiltViewModel()
            ContractsScreen(
                viewModel = contractsViewModel,
                onBackClick = { navController.popBackStack() },
                onSettingsClick = navigateToSettings
            )
        }

        composable(route = Screen.ContextReceivers.route) {
            val receiversViewModel: ContextReceiversViewModel = hiltViewModel()
            ContextReceiversScreen(
                viewModel = receiversViewModel,
                onBackClick = { navController.popBackStack() },
                onSettingsClick = navigateToSettings
            )
        }

        composable(route = Screen.CanvasAnimation.route) {
            val canvasViewModel: CanvasViewModel = hiltViewModel()
            CanvasScreen(
                viewModel = canvasViewModel,
                onBackClick = { navController.popBackStack() },
                onSettingsClick = navigateToSettings
            )
        }

        composable(route = Screen.MotionLayout.route) {
            val motionViewModel: MotionViewModel = hiltViewModel()
            MotionScreen(
                viewModel = motionViewModel,
                onBackClick = { navController.popBackStack() },
                onSettingsClick = navigateToSettings
            )
        }

        composable(route = Screen.MusicPlayer.route) {
            val musicViewModel: MusicPlayerViewModel = hiltViewModel()
            MusicPlayerScreen(
                viewModel = musicViewModel,
                onBackClick = { navController.popBackStack() },
                onSettingsClick = navigateToSettings
            )
        }

        composable(route = Screen.Testing.route) {
            val testingViewModel: TestingViewModel = hiltViewModel()
            TestingScreen(
                viewModel = testingViewModel,
                onBackClick = { navController.popBackStack() },
                onSettingsClick = navigateToSettings
            )
        }

        composable(route = Screen.GlassWallet.route) {
            val walletViewModel: WalletViewModel = hiltViewModel()
            GlassWalletScreen(
                viewModel = walletViewModel,
                onBackClick = { navController.popBackStack() },
                onSettingsClick = navigateToSettings
            )
        }

        composable(route = Screen.MorphLayout.route) {
            MorphScreen(
                onBackClick = { navController.popBackStack() },
                onSettingsClick = navigateToSettings
            )
        }
    }
}
