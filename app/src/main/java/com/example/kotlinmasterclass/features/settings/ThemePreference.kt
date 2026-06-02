package com.example.kotlinmasterclass.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinmasterclass.features.settings.data.ThemePreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ThemePreference { LIGHT, DARK, SYSTEM }

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: ThemePreferencesRepository
) : ViewModel() {

    val themePreference: StateFlow<ThemePreference?> = repository.themePreferenceFlow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null // Default while loading
        )

    fun updateTheme(preference: ThemePreference) {
        // Launch a coroutine to save to disk asynchronously
        viewModelScope.launch {
            repository.saveThemePreference(preference)
        }
    }
}