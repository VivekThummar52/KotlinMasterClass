package com.codecraft.kotlinmasterclass.features.canvas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CanvasViewModel @Inject constructor() : ViewModel() {

    // Simulating the live metrics you would get from a BroadcastReceiver
    private val _batteryLevel = MutableStateFlow(20f) // Starts at 20%
    val batteryLevel: StateFlow<Float> = _batteryLevel.asStateFlow()

    private val _isCharging = MutableStateFlow(false)
    val isCharging: StateFlow<Boolean> = _isCharging.asStateFlow()

    private var chargingJob: Job? = null

    fun toggleCharging() {
        _isCharging.value = !_isCharging.value

        if (_isCharging.value) {
            // Simulate a live charge cycle
            chargingJob = viewModelScope.launch {
                while (_batteryLevel.value < 100f) {
                    delay(800) // Speed up time for the sandbox
                    _batteryLevel.value += 1f
                }
                _isCharging.value = false // Stop charging at 100%
            }
        } else {
            chargingJob?.cancel()
        }
    }

    fun setBatteryLevel(level: Float) {
        _batteryLevel.value = level
        if (level >= 100f) {
            _isCharging.value = false
            chargingJob?.cancel()
        }
    }
}