package com.example.kotlinmasterclass.features.weather

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

// --- DOMAIN MODELS ---
enum class WeatherType { CLEAR, RAIN, STORM, CLOUDY }

data class GeoLocation(
    val id: Int, 
    val name: String, 
    val temperature: Int, 
    val type: WeatherType,
    val lat: Float, 
    val lon: Float
)

@HiltViewModel
class WeatherViewModel @Inject constructor() : ViewModel() {

    // A curated list of global hubs spanning different latitudes and longitudes
    val locations = listOf(
        GeoLocation(1, "Surat, IN", 34, WeatherType.CLEAR, 21.17f, 72.83f),
        GeoLocation(2, "Tokyo, JP", 22, WeatherType.RAIN, 35.67f, 139.65f),
        GeoLocation(3, "London, UK", 14, WeatherType.CLOUDY, 51.50f, -0.12f),
        GeoLocation(4, "New York, US", 18, WeatherType.STORM, 40.71f, -74.00f),
        GeoLocation(5, "Sydney, AU", 26, WeatherType.CLEAR, -33.86f, 151.20f)
    )

    private val _selectedLocation = MutableStateFlow(locations.first())
    val selectedLocation = _selectedLocation.asStateFlow()

    fun selectLocation(id: Int) {
        locations.find { it.id == id }?.let { loc ->
            _selectedLocation.update { loc }
        }
    }
}