package com.codecraft.kotlinmasterclass.features.coroutines

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CoroutinesViewModel @Inject constructor() : ViewModel() {

    private val TAG = "MasterclassLog"

    // Tracks the status text shown on the screen
    private val _uiState = MutableStateFlow("Tap an operation below to start.")
    val uiState: StateFlow<String> = _uiState.asStateFlow()

    // Tracks if an active job is running so the user can test cancellation
    private var trackableJob: Job? = null

    private var currentProgress = 0

    /**
     * Demonstrates Dispatchers and how they map to different thread pools.
     */
    fun runDispatcherDemo() {
        viewModelScope.launch(Dispatchers.Main) {
            _uiState.value = "Starting Dispatchers Demo..."
            Log.d(TAG, "Main Launch: Running on thread [${Thread.currentThread().name}]")

            // Switch to IO dispatcher for network/database simulation
            withContext(Dispatchers.IO) {
                Log.d(TAG, "withContext(Dispatchers.IO): Running on thread [${Thread.currentThread().name}]")
                delay(1000) // Simulating work
            }

            // Switch to Default dispatcher for CPU-intensive work
            withContext(Dispatchers.Default) {
                Log.d(TAG, "withContext(Dispatchers.Default): Running on thread [${Thread.currentThread().name}]")
                delay(1000)
            }

            Log.d(TAG, "Back to Main: Running on thread [${Thread.currentThread().name}]")
            _uiState.value = "Dispatchers Demo Completed. Check Logcat for thread logs!"
        }
    }

    /**
     * Demonstrates launch (fire-and-forget) vs async (returns a result via Deferred).
     */
    fun runLaunchVsAsyncDemo() {
        viewModelScope.launch {
            _uiState.value = "Executing Launch vs Async..."
            Log.d(TAG, "Demo Started")

            // 1. Launch builder
            val launchJob = launch {
                Log.d(TAG, "Launch Job: Working...")
                delay(1500)
                Log.d(TAG, "Launch Job: Completed (No return value)")
            }
            
            // Wait for launch job to complete
            launchJob.join()

            // 2. Async builder
            _uiState.value = "Launch finished. Executing Async operation..."
            val deferredResult: Deferred<String> = async {
                Log.d(TAG, "Async Job: Computing result...")
                delay(2000)
                "Hello from Async!" // Returned result
            }

            // Await the result
            val result = deferredResult.await()
            Log.d(TAG, "Async Result received: $result")
            _uiState.value = "Result: $result (Check Logcat for order of execution)"
        }
    }

    /**
     * Demonstrates how a Job can be explicitly cancelled mid-execution.
     */
    fun startCancellableJob() {
        // Cancel any existing job before starting a new one
        trackableJob?.cancel()
        currentProgress = 0 // Reset progress

        trackableJob = viewModelScope.launch(Dispatchers.Default) {
            try {
                _uiState.value = "Cancellable job started loop..."
                for (i in 1..10) {
                    ensureActive() 
                    
                    currentProgress = i * 10 // Update the tracked progress

                    Log.d(TAG, "Job execution loop counter: $i on [${Thread.currentThread().name}]")
                    withContext(Dispatchers.Main) {
                        _uiState.value = "Job Progress: $currentProgress%"
                    }
                    delay(1000)
                }
                _uiState.value = "Job completed naturally!"
            } catch (e: CancellationException) {
                Log.d(TAG, "Job caught CancellationException: ${e.message}")
                withContext(Dispatchers.Main + NonCancellable) {
                    // Display the exact percentage where it was cancelled
                    _uiState.value = if (currentProgress > 0) {
                        "Job cancelled at $currentProgress%"
                    } else {
                        "Job cancelled before starting."
                    }
                }
            }
        }
    }

    fun cancelActiveJob() {
        if (trackableJob?.isActive == true) {
            trackableJob?.cancel()
        } else {
            _uiState.value = "No active job running to cancel."
        }
    }
}