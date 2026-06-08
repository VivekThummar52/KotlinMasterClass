package com.example.kotlinmasterclass.features.concurrency

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

@HiltViewModel
class AdvancedConcurrencyViewModel @Inject constructor() : ViewModel() {

    private val TAG = "MasterclassLog"

    private val _uiState = MutableStateFlow("Tap a concurrency operation below to start.")
    val uiState: StateFlow<String> = _uiState.asStateFlow()

    /**
     * Demonstrates a Race Condition.
     * We launch 1,000 concurrent coroutines attempting to update a shared integer.
     * Because the operation (counter++) is not atomic, thread context switching causes data loss.
     */
    fun runRaceConditionDemo() {
        viewModelScope.launch(Dispatchers.Default) {
            _uiState.value = "Running 1000 unprotected coroutines..."
            var counter = 0
            
            // Launch 1000 coroutines concurrently
            val jobs = List(1000) {
                launch {
                    delay(10) // Force thread suspension to guarantee a context switch collision
                    counter++ 
                }
            }
            
            jobs.joinAll() // Wait for all 1000 to finish
            
            Log.d(TAG, "Unprotected Counter Result: $counter (Expected 1000)")
            _uiState.value = "Race Condition Result:\nExpected: 1000\nActual: $counter\n(Data was lost due to concurrent writes!)"
        }
    }

    /**
     * Demonstrates Thread Safety using Mutex (Mutual Exclusion).
     * The Mutex acts as a lock, ensuring only one coroutine can enter the critical section at a time.
     */
    fun runMutexDemo() {
        viewModelScope.launch(Dispatchers.Default) {
            _uiState.value = "Running 1000 protected coroutines using Mutex..."
            var counter = 0
            val mutex = Mutex() // Initialize the lock
            
            val jobs = List(1000) {
                launch {
                    delay(10)
                    // The critical section is locked. Other coroutines suspend until it's free.
                    mutex.withLock {
                        counter++
                    }
                }
            }
            
            jobs.joinAll()
            
            Log.d(TAG, "Mutex Protected Counter Result: $counter")
            _uiState.value = "Mutex Result:\nExpected: 1000\nActual: $counter\n(100% thread-safe!)"
        }
    }

    /**
     * Demonstrates Structured Exception Handling.
     * Normally, if a child coroutine crashes, it cancels its siblings and its parent.
     * supervisorScope isolates failures so sibling tasks can complete.
     */
    fun runExceptionHandlingDemo() {
        _uiState.value = "Starting SupervisorScope demo..."
        
        // Define a handler to catch the crash instead of crashing the app
        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            Log.e(TAG, "Caught exception safely: ${exception.message}")
        }

        // Attach the handler to the root launch
        viewModelScope.launch(exceptionHandler) {
            
            // Use supervisorScope to protect children from each other
            supervisorScope {
                
                // Child 1: Destined to fail
                launch {
                    delay(500)
                    Log.d(TAG, "Child 1: Simulating Network Crash now!")
                    throw RuntimeException("Fatal API Timeout")
                }

                // Child 2: Destined to succeed
                launch {
                    delay(1500)
                    Log.d(TAG, "Child 2: Completed successfully despite sibling crash!")
                    _uiState.value = "Exception Handling Result:\nChild 1 crashed, but Child 2 survived and completed thanks to supervisorScope!"
                }
            }
        }
    }
}