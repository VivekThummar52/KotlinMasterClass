package com.example.kotlinmasterclass.features.scopefunctions

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ScopeFunctionsViewModel @Inject constructor() : ViewModel() {

    private val TAG = "MasterclassLog"

    private val _uiState = MutableStateFlow("Tap a scope function below to see the result.")
    val uiState: StateFlow<String> = _uiState.asStateFlow()

    // Sample data model to demonstrate object configuration
    data class BatteryMetrics(
        var level: Int = 0,
        var temperature: Float = 0f,
        var isCharging: Boolean = false
    )

    /**
     * `let` context: 'it', returns: lambda result.
     * Best for: Null checks and executing code on non-null objects.
     */
    fun demonstrateLet() {
        val array: Array<String?> = arrayOf("Kotlin", null, "Masterclass", "Kotlin Masterclass", "")
//        val nullableString: String? = "Kotlin Masterclass"
        val nullableString: String? = array.randomOrNull()
        
        // let is perfect for executing a block only if the object is not null
        val length = nullableString?.let { 
            Log.d(TAG, "Inside let: String is '$it'")
            it.length // This is returned
        } ?: run {
            Log.d(TAG, "Inside let: String is null")
            0
        }

        _uiState.value = "`let` executed.\nReturned Length: $length\nCheck Logcat for details."
    }

    /**
     * `apply` context: 'this', returns: the context object itself.
     * Best for: Object initialization and configuration.
     */
    fun demonstrateApply() {
        // apply allows us to configure an object without repeating its name
        // It returns the newly configured object.
        val configuredMetrics = BatteryMetrics().apply {
            level = 85           // Notice we don't need 'this.level' or 'it.level'
            temperature = 34.5f
            isCharging = true
        }

        Log.d(TAG, "Apply result: $configuredMetrics")
        _uiState.value = "`apply` executed.\nConfigured Object: $configuredMetrics\n(Returned the object itself)"
    }

    /**
     * `also` context: 'it', returns: the context object itself.
     * Best for: Side effects (like logging) without altering the object.
     */
    fun demonstrateAlso() {
        // also is great for inserting a side effect (like logging) in a call chain
        val metrics = BatteryMetrics(100, 30.0f, false).also {
            Log.d(TAG, "Also logged side-effect: Battery is fully charged to ${it.level}%")
        }

        _uiState.value = "`also` executed.\nObject passed through: $metrics\n(Check Logcat for the side-effect log)"
    }

    /**
     * `run` context: 'this', returns: lambda result.
     * Best for: Operating on a non-null object and returning a computation.
     */
    fun demonstrateRun() {
        val metrics: BatteryMetrics? = BatteryMetrics(20, 42.0f, true)

        // run is often used combining null-safety with executing a block that returns a result
        val statusMessage = metrics?.run {
            // 'this' is available here
            if (temperature > 40.0f) {
                Log.d(TAG, "Run evaluated: Device is overheating!")
                "Warning: Temp is $temperature°C" // Returned result
            } else {
                "Normal: Temp is $temperature°C" // Returned result
            }
        } ?: "Metrics unavailable"

        _uiState.value = "`run` executed.\nReturned Status: $statusMessage"
    }

    /**
     * `with` context: 'this', returns: lambda result.
     * Best for: Grouping function calls on an object (not an extension function).
     */
    fun demonstrateWith() {
        val metrics = BatteryMetrics(50, 35.0f, false)

        // with is NOT an extension function. You pass the object as an argument.
        val summary = with(metrics) {
            Log.d(TAG, "Inside with: Level is $level, Charging is $isCharging")
            "Battery is at $level%." // Returned result
        }

        _uiState.value = "`with` executed.\nReturned Summary: $summary"
    }
}