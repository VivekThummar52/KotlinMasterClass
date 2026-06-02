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

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. Install the splash screen BEFORE super.onCreate()
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        // 2. Keep the splash screen visible as long as our theme is null (loading)
        splashScreen.setKeepOnScreenCondition {
            settingsViewModel.themePreference.value == null
        }

        setContent {
            val themePreference by settingsViewModel.themePreference.collectAsState()

            // 3. Only render the Compose UI once the theme is fully loaded
            themePreference?.let { currentTheme ->
                val isDarkTheme = when (currentTheme) {
                    ThemePreference.LIGHT -> false
                    ThemePreference.DARK -> true
                    ThemePreference.SYSTEM -> isSystemInDarkTheme()
                }

                KotlinMasterclassTheme(darkTheme = isDarkTheme) {
                    AppNavigation()
                }
            }
        }
    }
}