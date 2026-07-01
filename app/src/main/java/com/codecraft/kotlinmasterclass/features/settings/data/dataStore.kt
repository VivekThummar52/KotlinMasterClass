package com.codecraft.kotlinmasterclass.features.settings.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.codecraft.kotlinmasterclass.features.settings.ThemePreference
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Extension property to ensure a single instance of DataStore is created
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings_prefs")

@Singleton
class ThemePreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val THEME_KEY = stringPreferencesKey("theme_preference")

    // Read from DataStore, defaulting to SYSTEM if nothing is saved
    val themePreferenceFlow: Flow<ThemePreference> = context.dataStore.data.map { preferences ->
        val themeName = preferences[THEME_KEY] ?: ThemePreference.SYSTEM.name
        ThemePreference.valueOf(themeName)
    }

    // Write to DataStore
    suspend fun saveThemePreference(preference: ThemePreference) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = preference.name
        }
    }
}