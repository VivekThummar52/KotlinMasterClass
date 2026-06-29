package com.codecraft.kotlinmasterclass.features.delegation

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

// --- 1. CLASS DELEGATION SETUP ---
// Define an interface for behavior
interface AnalyticsLogger {
    fun logEvent(eventName: String)
}

// Create a concrete implementation
class ConsoleAnalyticsLogger : AnalyticsLogger {
    override fun logEvent(eventName: String) {
        Log.d("MasterclassLog", "📊 Analytics Tracked: $eventName")
    }
}

// --- 2. CUSTOM PROPERTY DELEGATE SETUP ---
// A custom delegate that automatically trims whitespace and capitalizes the first letter
class FormatStringDelegate(private var currentText: String = "") {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return currentText
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: String) {
        currentText = newValue.trim().replaceFirstChar { it.uppercase() }
        Log.d("MasterclassLog", "Custom Delegate intercepted and formatted: '$newValue' -> '$currentText'")
    }
}

@HiltViewModel
// Notice the 'by ConsoleAnalyticsLogger()'. 
// This ViewModel now implements AnalyticsLogger without needing to write the override function itself!
class DelegationViewModel @Inject constructor() : ViewModel(), AnalyticsLogger by ConsoleAnalyticsLogger() {

    private val TAG = "MasterclassLog"

    private val _uiState = MutableStateFlow("Tap a delegation option below to observe the behavior.")
    val uiState: StateFlow<String> = _uiState.asStateFlow()

    // --- 3. PROPERTY DELEGATION EXAMPLES ---

    // A. Lazy Initialization
    // The lambda is ONLY executed the first time 'heavyResource' is accessed.
    private val heavyResource: String by lazy {
        Log.d(TAG, "Initializing heavy resource (This only happens once!)...")
        "Heavy Database Connection Established"
    }

    // B. Observable Delegate
    // Triggers a callback every time the variable is modified
    private var observedUsername: String by Delegates.observable("Guest") { property, old, new ->
        Log.d(TAG, "Observable: '${property.name}' changed from '$old' to '$new'")
        _uiState.value = "Observable Delegate:\nUsername updated to '$new'.\n(Check Logcat for intercept details)"
    }

    // C. Vetoable Delegate
    // Triggers a callback BEFORE the change. If the lambda returns false, the change is rejected.
    private var age: Int by Delegates.vetoable(0) { _, old, new ->
        val isValid = new >= 0
        if (!isValid) Log.d(TAG, "Vetoable: Rejected invalid age '$new'")
        isValid // Must be non-negative
    }

    // D. Custom Delegate
    private var formattedInput: String by FormatStringDelegate()

    // --- EXECUTION FUNCTIONS ---

    fun runClassDelegationDemo() {
        // We can call logEvent directly because the ViewModel delegated the implementation!
        logEvent("Class_Delegation_Triggered")
        _uiState.value = "Class Delegation Executed:\nThe ViewModel logged an analytics event without implementing the code itself. Check Logcat!"
    }

    fun runLazyDelegateDemo() {
        _uiState.value = "Lazy Delegate:\nAccessing resource: $heavyResource\n(Tap again to see that the initialization log doesn't repeat in Logcat)"
    }

    fun runObservableDelegateDemo() {
        val newNames = listOf("Vivek", "AndroidDev", "KotlinMaster")
        observedUsername = newNames.random()
    }

    fun runVetoableDelegateDemo() {
        val testAges = listOf(25, -5, 30)
        val selectedAge = testAges.random()
        
        age = selectedAge // Will only update if >= 0
        
        _uiState.value = "Vetoable Delegate:\nAttempted to set age to $selectedAge.\nCurrent age in memory is now: $age"
    }

    fun runCustomDelegateDemo() {
        val rawInput = "   messy input string   "
        formattedInput = rawInput // The delegate intercepts this assignment!
        
        _uiState.value = "Custom Delegate:\nRaw Assignment: '$rawInput'\nStored Value: '$formattedInput'"
    }
}