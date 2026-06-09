package com.example.kotlinmasterclass.features.smarthome

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

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