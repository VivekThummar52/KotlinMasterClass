package com.example.kotlinmasterclass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen // Add this import
import com.example.kotlinmasterclass.features.settings.SettingsViewModel
import com.example.kotlinmasterclass.features.settings.ThemePreference
import com.example.kotlinmasterclass.navigation.AppNavigation
import com.example.kotlinmasterclass.ui.theme.KotlinMasterclassTheme
import dagger.hilt.android.AndroidEntryPoint

import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.LaunchedEffect
import com.example.kotlinmasterclass.navigation.Screen
import android.content.Intent

import androidx.compose.runtime.mutableStateOf

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels()
    private var pendingNavigation = mutableStateOf<String?>(null)

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        pendingNavigation.value = intent.getStringExtra("NAVIGATE_TO")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. Install the splash screen BEFORE super.onCreate()
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        
        pendingNavigation.value = intent.getStringExtra("NAVIGATE_TO")

        // 2. Keep the splash screen visible as long as our theme is null (loading)
        splashScreen.setKeepOnScreenCondition {
            settingsViewModel.themePreference.value == null
        }

        setContent {
            val themePreference by settingsViewModel.themePreference.collectAsState()
            val navController = rememberNavController()
            val navigationTarget by pendingNavigation

            // 3. Handle incoming navigation requests (from other activities)
            LaunchedEffect(navigationTarget, themePreference) {
                if (themePreference != null && navigationTarget == "settings") {
                    navController.navigate(Screen.Settings.route) {
                        launchSingleTop = true
                    }
                    // Clear the pending navigation so it doesn't navigate again on recomposition
                    pendingNavigation.value = null
                }
            }

            // 4. Only render the Compose UI once the theme is fully loaded
            themePreference?.let { currentTheme ->
                val isDarkTheme = when (currentTheme) {
                    ThemePreference.LIGHT -> false
                    ThemePreference.DARK -> true
                    ThemePreference.SYSTEM -> isSystemInDarkTheme()
                }

                KotlinMasterclassTheme(darkTheme = isDarkTheme) {
                    AppNavigation(navController = navController)
                }
            }
        }
    }
}