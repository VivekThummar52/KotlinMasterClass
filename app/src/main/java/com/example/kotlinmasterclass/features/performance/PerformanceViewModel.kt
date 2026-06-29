package com.example.kotlinmasterclass.features.performance

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.system.measureTimeMillis

// --- VALUE CLASS DEFINITION ---
// The @JvmInline annotation and 'value class' keyword tell the compiler to treat this
// as a strict type during coding, but compile it down to just a raw String at runtime.
// No object allocation happens on the heap!
@JvmInline
value class SecureToken(val token: String)

@HiltViewModel
class PerformanceViewModel @Inject constructor() : ViewModel() {

    private val TAG = "MasterclassLog"

    private val _uiState = MutableStateFlow("Tap a benchmark below to run performance tests.")
    val uiState: StateFlow<String> = _uiState.asStateFlow()

    // A massive dataset to make the performance difference obvious
    private val massiveList = (1..500_000).toList()

    /**
     * Demonstrates Eager Evaluation (Iterables).
     * Every operation (map, filter) creates a brand new intermediate List in memory.
     */
    fun runIterableBenchmark() {
        _uiState.value = "Running Iterable Benchmark (Please wait)..."
        
        viewModelScope.launch(Dispatchers.Default) {
            val time = measureTimeMillis {
                // Eager: Creates a full list of 500k, then a full filtered list, then takes 10.
                val result = massiveList
                    .map { it * 2 }
                    .filter { it % 3 == 0 }
                    .take(10)
                
                Log.d(TAG, "Iterable Result: $result")
            }

            withContext(Dispatchers.Main) {
                Log.d(TAG, "Iterable took: $time ms")
                _uiState.value = "Iterable (Eager) Benchmark:\nProcessed 500,000 items.\nTime taken: $time ms\n(High memory footprint due to intermediate lists)"
            }
        }
    }

    /**
     * Demonstrates Lazy Evaluation (Sequences).
     * Operations are applied element-by-element. No intermediate lists are created.
     */
    fun runSequenceBenchmark() {
        _uiState.value = "Running Sequence Benchmark (Please wait)..."
        
        viewModelScope.launch(Dispatchers.Default) {
            val time = measureTimeMillis {
                // Lazy: Looks at item 1, maps it, filters it. Then item 2. 
                // It stops COMPLETELY as soon as it finds 10 valid items.
                val result = massiveList.asSequence()
                    .map { it * 2 }
                    .filter { it % 3 == 0 }
                    .take(10)
                    .toList() // Terminal operator actually triggers the work
                
                Log.d(TAG, "Sequence Result: $result")
            }

            withContext(Dispatchers.Main) {
                Log.d(TAG, "Sequence took: $time ms")
                _uiState.value = "Sequence (Lazy) Benchmark:\nProcessed items selectively.\nTime taken: $time ms\n(Extremely fast and memory efficient!)"
            }
        }
    }

    /**
     * Demonstrates strict typing without object allocation overhead.
     */
    fun runValueClassDemo() {
        // At compile-time, rawString and secureToken are different types.
        // At runtime, the JVM just sees two Strings. Zero wrapper objects are allocated.
        val rawString = "abc-123"
        val secureToken = SecureToken("abc-123")

        // authenticate(rawString) // -> COMPILE ERROR! Strict type safety enforced.
        authenticate(secureToken) // -> Compiles successfully.

        _uiState.value = "Value Class (Inline) Demo:\n" +
                "Created a SecureToken.\n" +
                "Compile-time: Strict Type Safety enforced.\n" +
                "Runtime: Zero object allocation (compiles down to a primitive String)."
    }

    private fun authenticate(token: SecureToken) {
        Log.d(TAG, "Authenticated with token: ${token.token}")
    }
}