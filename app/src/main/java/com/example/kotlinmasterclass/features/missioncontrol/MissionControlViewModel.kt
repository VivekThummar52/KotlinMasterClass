package com.example.kotlinmasterclass.features.missioncontrol

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MissionControlViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(MissionControlState())
    val uiState: StateFlow<MissionControlState> = _uiState.asStateFlow()

    private var simulationJob: Job? = null

    fun startReactor() {
        _uiState.update(MissionControlEngine::start)
        if (simulationJob?.isActive == true) return

        simulationJob = viewModelScope.launch {
            while (true) {
                delay(SIMULATION_INTERVAL_MS)
                _uiState.update(MissionControlEngine::tick)
            }
        }
    }

    fun setTargetPower(value: Float) {
        _uiState.update { MissionControlEngine.setTargetPower(it, value) }
    }

    fun setCoolant(value: Float) {
        _uiState.update { MissionControlEngine.setCoolant(it, value) }
    }

    fun toggleShields() {
        _uiState.update(MissionControlEngine::toggleShields)
    }

    fun toggleBoost() {
        _uiState.update(MissionControlEngine::toggleBoost)
    }

    fun stabilize() {
        _uiState.update(MissionControlEngine::stabilize)
    }

    fun emergencyShutdown() {
        simulationJob?.cancel()
        simulationJob = null
        _uiState.update(MissionControlEngine::emergencyShutdown)
    }

    private companion object {
        const val SIMULATION_INTERVAL_MS = 420L
    }
}
