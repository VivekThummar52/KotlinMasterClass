package com.codecraft.kotlinmasterclass.features.observatory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class AiPhase { IDLE, ROUTING, REASONING, TOOL_CALL, SYNTHESIS, COMPLETE, FAILED_RECOVERY }
enum class NodeState { IDLE, ACTIVE, SUCCESS, FAILED }

data class AiMetrics(
    val latencyMs: Int = 0,
    val inputTokens: Int = 0,
    val outputTokens: Int = 0,
    val activeAgents: Int = 0
)

data class ObservatoryState(
    val phase: AiPhase = AiPhase.IDLE,
    val metrics: AiMetrics = AiMetrics(),
    val prompt: String = "Waiting for input...",
    val nodes: Map<String, NodeState> = defaultNodes,
    val activePulses: List<Pulse> = emptyList(),
    val isRunning: Boolean = false,
    val failureInjected: Boolean = false
)

data class Pulse(val from: String, val to: String, val progress: Float = 0f)

val defaultNodes = mapOf(
    "router" to NodeState.IDLE,
    "agent_search" to NodeState.IDLE,
    "agent_logic" to NodeState.IDLE,
    "tool_web" to NodeState.IDLE,
    "tool_db" to NodeState.IDLE,
    "synthesizer" to NodeState.IDLE
)

@HiltViewModel
class AiObservatoryViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ObservatoryState())
    val uiState: StateFlow<ObservatoryState> = _uiState.asStateFlow()

    private var simJob: Job? = null
    private var metricJob: Job? = null

    fun toggleFailureInjection() {
        _uiState.update { it.copy(failureInjected = !it.failureInjected) }
    }

    fun startSimulation() {
        if (_uiState.value.isRunning) return
        simJob?.cancel()
        metricJob?.cancel()

        _uiState.value = ObservatoryState(
            isRunning = true,
            prompt = "Analyze global market trends and compile a summary.",
            failureInjected = _uiState.value.failureInjected
        )

        startMetricsTimer()

        simJob = viewModelScope.launch {
            // PHASE 1: ROUTING
            updatePhase(AiPhase.ROUTING)
            setNode("router", NodeState.ACTIVE)
            firePulse("user", "router")
            addTokens(input = 45, output = 12)
            delay(800)
            setNode("router", NodeState.SUCCESS)

            // PHASE 2: REASONING (Parallel Agents)
            updatePhase(AiPhase.REASONING)
            setNode("agent_search", NodeState.ACTIVE)
            setNode("agent_logic", NodeState.ACTIVE)
            firePulse("router", "agent_search")
            firePulse("router", "agent_logic")
            addTokens(input = 0, output = 150)
            delay(1200)

            // CHAOS EVENT: Inject Failure
            if (_uiState.value.failureInjected) {
                updatePhase(AiPhase.FAILED_RECOVERY)
                setNode("agent_search", NodeState.FAILED)
                delay(600) // System detects failure
                
                // Reroute to Logic Agent instead
                firePulse("router", "tool_db") // Fallback route
                setNode("tool_db", NodeState.ACTIVE)
                addTokens(input = 0, output = 45)
                delay(1000)
                setNode("tool_db", NodeState.SUCCESS)
            } else {
                // Normal Tool Call Phase
                updatePhase(AiPhase.TOOL_CALL)
                setNode("agent_search", NodeState.SUCCESS)
                setNode("agent_logic", NodeState.SUCCESS)
                
                setNode("tool_web", NodeState.ACTIVE)
                setNode("tool_db", NodeState.ACTIVE)
                firePulse("agent_search", "tool_web")
                firePulse("agent_logic", "tool_db")
                addTokens(input = 800, output = 45)
                delay(1500)
                setNode("tool_web", NodeState.SUCCESS)
                setNode("tool_db", NodeState.SUCCESS)
            }

            // PHASE 4: SYNTHESIS
            updatePhase(AiPhase.SYNTHESIS)
            setNode("synthesizer", NodeState.ACTIVE)
            val sourceNodes = if (_uiState.value.failureInjected) listOf("agent_logic", "tool_db") else listOf("tool_web", "tool_db")
            sourceNodes.forEach { firePulse(it, "synthesizer") }
            
            addTokens(input = 400, output = 350)
            delay(1800)
            setNode("synthesizer", NodeState.SUCCESS)

            // COMPLETE
            updatePhase(AiPhase.COMPLETE)
            metricJob?.cancel()
            _uiState.update { it.copy(isRunning = false, prompt = "Analysis complete. Output generated successfully.") }
        }
    }

    private fun startMetricsTimer() {
        metricJob = viewModelScope.launch {
            val startTime = System.currentTimeMillis()
            while (true) {
                delay(50)
                val currentMs = (System.currentTimeMillis() - startTime).toInt()
                _uiState.update { 
                    it.copy(metrics = it.metrics.copy(
                        latencyMs = currentMs,
                        activeAgents = it.nodes.values.count { state -> state == NodeState.ACTIVE }
                    )) 
                }
            }
        }
    }

    private fun updatePhase(phase: AiPhase) {
        _uiState.update { it.copy(phase = phase) }
    }

    private fun setNode(id: String, state: NodeState) {
        _uiState.update { it.copy(nodes = it.nodes.toMutableMap().apply { put(id, state) }) }
    }

    private fun addTokens(input: Int, output: Int) {
        _uiState.update { 
            val m = it.metrics
            it.copy(metrics = m.copy(inputTokens = m.inputTokens + input, outputTokens = m.outputTokens + output))
        }
    }

    // Instead of complex UI animators, the engine calculates the pulse progress mathematically
    // so it perfectly syncs with the simulation state.
    private fun firePulse(from: String, to: String) {
        viewModelScope.launch {
            val pulseId = "$from-$to-${System.currentTimeMillis()}"
            for (i in 0..100 step 2) { // Moves 0.0 to 1.0
                delay(16) // ~60fps
                updatePulse(from, to, i / 100f)
            }
            // Remove pulse
            _uiState.update { it.copy(activePulses = it.activePulses.filterNot { p -> p.from == from && p.to == to }) }
        }
    }

    private fun updatePulse(from: String, to: String, progress: Float) {
        _uiState.update { state ->
            val existing = state.activePulses.filterNot { it.from == from && it.to == to }
            state.copy(activePulses = existing + Pulse(from, to, progress))
        }
    }
}