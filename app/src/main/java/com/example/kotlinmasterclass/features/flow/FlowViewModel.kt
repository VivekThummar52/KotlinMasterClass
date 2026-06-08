package com.example.kotlinmasterclass.features.flow

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlowViewModel @Inject constructor() : ViewModel() {

    private val TAG = "MasterclassLog"

    // 1. StateFlow: Holds a single state, requires an initial value, and conflates emissions (keeps only the latest).
    // Ideal for UI State.
    private val _uiState = MutableStateFlow("Tap a Flow operation below to start.")
    val uiState: StateFlow<String> = _uiState.asStateFlow()

    // 2. SharedFlow: Emits events, does NOT require an initial value, and does not hold state by default.
    // Ideal for one-time events (Snackbars, Navigation).
    private val _eventFlow = MutableSharedFlow<String>()
    val eventFlow: SharedFlow<String> = _eventFlow.asSharedFlow()

    /**
     * Demonstrates a Cold Flow. The code inside the builder does NOT run until terminal operator (.collect) is called.
     * It runs from scratch for EVERY new collector.
     */
    fun runColdFlowDemo() {
        viewModelScope.launch {
            _uiState.value = "Starting Cold Flow..."
            
            val coldFlow = flow {
                Log.d(TAG, "Cold Flow Emitting...")
                emit("A")
                delay(500)
                emit("B")
                delay(500)
                emit("C")
            }

            Log.d(TAG, "--- Collector 1 Started ---")
            coldFlow.collect { Log.d(TAG, "Collector 1 received: $it") }

            Log.d(TAG, "--- Collector 2 Started ---")
            // Notice in the logs that the flow runs completely from the beginning again!
            coldFlow.collect { Log.d(TAG, "Collector 2 received: $it") }

            _uiState.value = "Cold Flow collected twice. Check Logcat to see it restart from scratch each time!"
        }
    }

    /**
     * Demonstrates emitting a one-time event using SharedFlow.
     */
    fun triggerSharedFlowEvent() {
        viewModelScope.launch {
            val timestamp = System.currentTimeMillis()
            Log.d(TAG, "Emitting SharedFlow Event at $timestamp")
            
            // emit() suspends until all subscribers receive it. 
            _eventFlow.emit("One-time Event Triggered!\nTimestamp: $timestamp")
            _uiState.value = "Event emitted to SharedFlow. The UI caught it below!"
        }
    }

    /**
     * Demonstrates how Flow operators transform data streams continuously.
     */
    fun runFlowOperatorsDemo() {
        viewModelScope.launch {
            _uiState.value = "Running Flow Operators..."
            
            val numbersFlow = flowOf(1, 2, 3, 4, 5)
            var lastValue = 0

            numbersFlow
                .onEach { delay(500) } // Simulate network/DB delay
                .filter { it % 2 != 0 } // Keep only ODD numbers (1, 3, 5)
                .map { it * 10 } // Transform: Multiply by 10 (10, 30, 50)
                .onCompletion {
                    Log.d(TAG, "Flow Pipeline Completed")
                    _uiState.value = "Operator Processing Completed...\nLast Value: $lastValue"
                }
                .collect { result ->
                    Log.d(TAG, "Operator result collected: $result")
                    _uiState.value = "Operator Processing...\nLatest Value: $result"
                    lastValue = result
                }
        }
    }
}