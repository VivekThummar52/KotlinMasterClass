package com.codecraft.kotlinmasterclass.features.smarthome

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

// --- THEME STATE ---
data class SmartHomeColors(
    val bg: Color,
    val lightShadow: Color,
    val darkShadow: Color,
    val accent: Color,
    val textMain: Color,
    val textMuted: Color
)

// --- DOMAIN MODELS ---
data class SmartDevice(
    val id: String,
    val name: String,
    val iconName: String,
    val isActive: Boolean = false
)

data class SmartHomeState(
    val temperature: Float = 22f,
    val activeRoomId: String = "Living Room",
    val devices: List<SmartDevice> = listOf(
        SmartDevice("1", "Main Lights", "Light", true),
        SmartDevice("2", "Thermostat AC", "AC", false),
        SmartDevice("3", "OLED TV", "TV", true),
        SmartDevice("4", "Sound System", "Speaker", false)
    )
)

@HiltViewModel
class SmartHomeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SmartHomeState())
    val uiState = _uiState.asStateFlow()

    fun setTemperature(temp: Float) {
        _uiState.update { it.copy(temperature = temp.coerceIn(16f, 32f)) }
    }

    fun toggleDevice(deviceId: String) {
        _uiState.update { state ->
            val updatedDevices = state.devices.map { 
                if (it.id == deviceId) it.copy(isActive = !it.isActive) else it
            }
            state.copy(devices = updatedDevices)
        }
    }

    fun selectRoom(roomId: String) {
        _uiState.update { it.copy(activeRoomId = roomId) }
    }
}