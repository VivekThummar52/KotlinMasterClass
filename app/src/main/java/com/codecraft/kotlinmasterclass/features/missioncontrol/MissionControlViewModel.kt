package com.codecraft.kotlinmasterclass.features.missioncontrol

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

                // --- ADD THIS AI INTERCEPTION BLOCK ---
                val currentState = _uiState.value
                if (currentState.autopilot.isEngaged && !currentState.autopilot.isIntervening && currentState.isRunning) {
                    if (currentState.stability < 68f || currentState.temperature > 88f) {
                        launchAiIntervention(currentState)
                    }
                }
                // --------------------------------------
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

    // 1. Add the Toggle Function
    fun toggleAutopilot() {
        _uiState.update {
            val newState = !it.autopilot.isEngaged
            val message = if (newState) "AI Autopilot Engaged. Monitoring telemetry." else "AI Autopilot Offline. Manual control required."
            val severity = if (newState) EventSeverity.INFO else EventSeverity.WARNING

            // Re-use your engine's internal event logger
            var nextState = it.copy(autopilot = it.autopilot.copy(isEngaged = newState, isIntervening = false, activeNodes = emptySet(), pulses = emptyList()))
            nextState = nextState.copy(events = (listOf(MissionEvent(nextState.tick, message, severity)) + nextState.events).take(8))
            nextState
        }
    }

    // 2. Add this inside your existing startReactor() simulation loop:
    /*
        simulationJob = viewModelScope.launch {
            while (true) {
                delay(SIMULATION_INTERVAL_MS)
                _uiState.update(MissionControlEngine::tick)

                // --- ADD THIS AI INTERCEPTION BLOCK ---
                val currentState = _uiState.value
                if (currentState.autopilot.isEngaged && !currentState.autopilot.isIntervening && currentState.isRunning) {
                    if (currentState.stability < 68f || currentState.temperature > 88f) {
                        launchAiIntervention(currentState)
                    }
                }
                // --------------------------------------
            }
        }
    */

    // 3. Add the Autonomous Intervention Engine
    private fun launchAiIntervention(currentState: MissionControlState) {
        viewModelScope.launch {
            _uiState.update { it.copy(autopilot = it.autopilot.copy(isIntervening = true)) }

            // Decide which system needs fixing
            val targetNode = if (currentState.stability < 68f) "containment" else "coolant"

            // Phase 1: Ingest Data (Pulse from Sensor to Analyzer)
            animatePulse("sensor", "analyzer")

            // Phase 2: Route Command (Pulse from Analyzer to Target System)
            animatePulse("analyzer", targetNode)

            // Phase 3: Execute Action autonomously!
            if (targetNode == "containment") {
                stabilize()
            } else {
                setCoolant(100f)
            }

            // Phase 4: Cooldown and reset the neural graph
            delay(1500)
            _uiState.update { it.copy(autopilot = it.autopilot.copy(isIntervening = false, activeNodes = emptySet())) }
        }
    }

    // Mathematical interpolation to move the pulse perfectly across the screen at 60fps
    private suspend fun animatePulse(from: String, to: String) {
        _uiState.update { it.copy(autopilot = it.autopilot.copy(activeNodes = it.autopilot.activeNodes + from)) }

        for (i in 0..100 step 4) { // Move 4% every 16ms
            delay(16)
            _uiState.update {
                val p = Pulse(from, to, i / 100f)
                it.copy(autopilot = it.autopilot.copy(pulses = listOf(p)))
            }
        }

        // Lock the destination node as active
        _uiState.update {
            it.copy(autopilot = it.autopilot.copy(
                pulses = emptyList(),
                activeNodes = it.autopilot.activeNodes + to
            ))
        }
    }
}
