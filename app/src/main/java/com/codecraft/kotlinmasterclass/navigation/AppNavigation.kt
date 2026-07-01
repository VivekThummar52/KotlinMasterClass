package com.codecraft.kotlinmasterclass.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.codecraft.kotlinmasterclass.features.jobdiscovery.JobDiscoveryActivity
import com.codecraft.kotlinmasterclass.features.audio.AudioStudioScreen
import com.codecraft.kotlinmasterclass.features.audio.AudioStudioViewModel
import com.codecraft.kotlinmasterclass.features.calculator.GraphingCalculatorScreen
import com.codecraft.kotlinmasterclass.features.calculator.GraphingViewModel
import com.codecraft.kotlinmasterclass.features.canvas.CanvasScreen
import com.codecraft.kotlinmasterclass.features.canvas.CanvasViewModel
import com.codecraft.kotlinmasterclass.features.concurrency.AdvancedConcurrencyScreen
import com.codecraft.kotlinmasterclass.features.concurrency.AdvancedConcurrencyViewModel
import com.codecraft.kotlinmasterclass.features.contextreceivers.ContextReceiversScreen
import com.codecraft.kotlinmasterclass.features.contextreceivers.ContextReceiversViewModel
import com.codecraft.kotlinmasterclass.features.contracts.ContractsScreen
import com.codecraft.kotlinmasterclass.features.contracts.ContractsViewModel
import com.codecraft.kotlinmasterclass.features.coroutines.CoroutinesScreen
import com.codecraft.kotlinmasterclass.features.coroutines.CoroutinesViewModel
import com.codecraft.kotlinmasterclass.features.dashboard.DashboardScreen
import com.codecraft.kotlinmasterclass.features.delegation.DelegationScreen
import com.codecraft.kotlinmasterclass.features.delegation.DelegationViewModel
import com.codecraft.kotlinmasterclass.features.extensionfunctions.ExtensionFunctionsScreen
import com.codecraft.kotlinmasterclass.features.extensionfunctions.ExtensionFunctionsViewModel
import com.codecraft.kotlinmasterclass.features.finance.FinanceCommandCenterScreen
import com.codecraft.kotlinmasterclass.features.finance.FinanceViewModel
import com.codecraft.kotlinmasterclass.features.flow.FlowScreen
import com.codecraft.kotlinmasterclass.features.flow.FlowViewModel
import com.codecraft.kotlinmasterclass.features.generics.GenericsScreen
import com.codecraft.kotlinmasterclass.features.generics.GenericsViewModel
import com.codecraft.kotlinmasterclass.features.higherorderfunctions.HigherOrderFunctionsScreen
import com.codecraft.kotlinmasterclass.features.higherorderfunctions.HigherOrderFunctionsViewModel
import com.codecraft.kotlinmasterclass.features.morph.MorphScreen
import com.codecraft.kotlinmasterclass.features.motion.MotionScreen
import com.codecraft.kotlinmasterclass.features.motion.MotionViewModel
import com.codecraft.kotlinmasterclass.features.musicplayer.MusicPlayerScreen
import com.codecraft.kotlinmasterclass.features.musicplayer.MusicPlayerViewModel
import com.codecraft.kotlinmasterclass.features.missioncontrol.MissionControlScreen
import com.codecraft.kotlinmasterclass.features.missioncontrol.MissionControlViewModel
import com.codecraft.kotlinmasterclass.features.myhealthvitals.MyHealthActivity
import com.codecraft.kotlinmasterclass.features.myspendings.MySpendingsActivity
import com.codecraft.kotlinmasterclass.features.observatory.AiObservatoryScreen
import com.codecraft.kotlinmasterclass.features.observatory.AiObservatoryViewModel
import com.codecraft.kotlinmasterclass.features.performance.PerformanceScreen
import com.codecraft.kotlinmasterclass.features.performance.PerformanceViewModel
import com.codecraft.kotlinmasterclass.features.scopefunctions.ScopeFunctionsScreen
import com.codecraft.kotlinmasterclass.features.scopefunctions.ScopeFunctionsViewModel
import com.codecraft.kotlinmasterclass.features.sealedclasses.SealedClassesScreen
import com.codecraft.kotlinmasterclass.features.sealedclasses.SealedClassesViewModel
import com.codecraft.kotlinmasterclass.features.settings.SettingsScreen
import com.codecraft.kotlinmasterclass.features.settings.SettingsViewModel
import com.codecraft.kotlinmasterclass.features.smartcity.SmartCityScreen
import com.codecraft.kotlinmasterclass.features.smartcity.SmartCityViewModel
import com.codecraft.kotlinmasterclass.features.smarthome.SmartHomeScreen
import com.codecraft.kotlinmasterclass.features.smarthome.SmartHomeViewModel
import com.codecraft.kotlinmasterclass.features.testing.TestingScreen
import com.codecraft.kotlinmasterclass.features.wallet.GlassWalletScreen
import com.codecraft.kotlinmasterclass.features.wallet.WalletViewModel
import com.codecraft.kotlinmasterclass.features.weather.WeatherPlanetariumScreen
import com.codecraft.kotlinmasterclass.features.weather.WeatherViewModel
import kotlin.jvm.java

import androidx.navigation.NavHostController
import com.codecraft.kotlinmasterclass.features.aicompanion.AICompanionActivity
import com.codecraft.kotlinmasterclass.features.digitalwallet.DigitalWalletActivity
import com.codecraft.kotlinmasterclass.features.testing.TestingViewModel

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    val navigateToSettings = {
        navController.navigate(Screen.Settings.route) {
            launchSingleTop = true
        }
    }

    NavHost(navController = navController, startDestination = Screen.Dashboard.route) {

        composable(route = Screen.Dashboard.route) {
            val context = LocalContext.current
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
                onNavigateToAiObservatory = { navController.navigate(Screen.AiObservatory.route) },
                onNavigateToSmartCity = { navController.navigate(Screen.SmartCity.route) },
                onNavigateToWeather = { navController.navigate(Screen.WeatherPlanetarium.route) },
                onNavigateToAudioStudio = { navController.navigate(Screen.AudioStudio.route) },
                onNavigateToFinance = { navController.navigate(Screen.FinanceCommandCenter.route) },
                onNavigateToSmartHome = { navController.navigate(Screen.SmartHome.route) },
                onNavigateToGraphingCalculator = { navController.navigate(Screen.GraphingCalculatorScreen.route) },
                onLaunchJobDiscovery = {
                    val intent = Intent(context, JobDiscoveryActivity::class.java)
                    context.startActivity(intent)
                },
                onLaunchMySpendings = {
                    val intent = Intent(context, MySpendingsActivity::class.java)
                    context.startActivity(intent)
                },
                onLaunchMyHealth = {
                    val intent = Intent(context, MyHealthActivity::class.java)
                    context.startActivity(intent)
                },
                onLaunchAICompanion = {
                    val intent = Intent(context, AICompanionActivity::class.java)
                    context.startActivity(intent)
                },
                onLaunchWalletActivity = {
                    val intent = Intent(context, DigitalWalletActivity::class.java)
                    context.startActivity(intent)
                },
                onNavigateToSettings = navigateToSettings)
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

        composable(route = Screen.MissionControl.route) {
            val missionControlViewModel: MissionControlViewModel = hiltViewModel()
            MissionControlScreen(
                viewModel = missionControlViewModel,
                onBackClick = { navController.popBackStack() },
                onSettingsClick = navigateToSettings
            )
        }

        composable(route = Screen.AiObservatory.route) {
            val aiViewModel: AiObservatoryViewModel = hiltViewModel()
            AiObservatoryScreen(
                viewModel = aiViewModel,
                onBackClick = { navController.popBackStack() },
                onSettingsClick = navigateToSettings
            )
        }

        composable(route = Screen.SmartCity.route) {
            val smartCityViewModel: SmartCityViewModel = hiltViewModel()
            SmartCityScreen(
                viewModel = smartCityViewModel,
                onBackClick = { navController.popBackStack() },
                onSettingsClick = navigateToSettings
            )
        }

        composable(route = Screen.WeatherPlanetarium.route) {
            val weatherViewModel: WeatherViewModel = hiltViewModel()
            WeatherPlanetariumScreen(
                viewModel = weatherViewModel,
                onBackClick = { navController.popBackStack() },
                onSettingsClick = navigateToSettings
            )
        }

        composable(route = Screen.AudioStudio.route) {
            val audioViewModel: AudioStudioViewModel = hiltViewModel()
            AudioStudioScreen(
                viewModel = audioViewModel,
                onBackClick = { navController.popBackStack() },
                onSettingsClick = navigateToSettings
            )
        }

        composable(route = Screen.FinanceCommandCenter.route) {
            val financeViewModel: FinanceViewModel = hiltViewModel()
            FinanceCommandCenterScreen(
                viewModel = financeViewModel,
                onBackClick = { navController.popBackStack() },
                onSettingsClick = navigateToSettings
            )
        }

        composable(route = Screen.SmartHome.route) {
            val viewModel: SmartHomeViewModel = hiltViewModel()
            SmartHomeScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onSettingsClick = navigateToSettings
            )
        }

        composable(route = Screen.GraphingCalculatorScreen.route) {
            val viewModel: GraphingViewModel = hiltViewModel()
            GraphingCalculatorScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onSettingsClick = navigateToSettings
            )
        }
    }
}
