package com.codecraft.kotlinmasterclass.features.contextreceivers

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

// --- 1. DEFINE THE CONTEXTS ---
interface AnalyticsLogger {
    fun logEvent(event: String) {
        Log.d("MasterclassLog", "📊 Analytics: $event")
    }
}

interface DatabaseTransaction {
    val transactionId: String
    fun commit(data: String) {
        Log.d("MasterclassLog", "💾 DB [$transactionId]: Committed '$data'")
    }
}

// Concrete implementations for our execution scope
class DefaultLogger : AnalyticsLogger
class ActiveTransaction(override val transactionId: String) : DatabaseTransaction

@HiltViewModel
class ContextReceiversViewModel @Inject constructor() : ViewModel() {

    private val TAG = "MasterclassLog"

    private val _uiState = MutableStateFlow("Tap the execution button to test Context Receivers.")
    val uiState: StateFlow<String> = _uiState.asStateFlow()

    // --- 2. THE CONTEXT RECEIVER FUNCTION ---
    // This function can ONLY be called if both an AnalyticsLogger AND a DatabaseTransaction are currently in scope.
    // Notice we do NOT pass them as parameters inside the parentheses!
    context(AnalyticsLogger, DatabaseTransaction)
    private fun processPayment(amount: Double) {
        // We have direct access to 'logEvent' from the AnalyticsLogger context
        logEvent("PaymentProcessingStarted")
        
        // We have direct access to 'commit' and 'transactionId' from the DatabaseTransaction context
        commit("Processed ₹$amount")
        
        logEvent("PaymentProcessingFinished")
    }

    fun runContextReceiverDemo() {
        val logger = DefaultLogger()
        val txSession = ActiveTransaction("TXN-7734")

        // processPayment(500.0) // ❌ COMPILE ERROR: No context provided!

        // --- 3. BRINGING CONTEXTS INTO SCOPE ---
        // We use 'with' to inject the required contexts into the current execution block
        with(logger) {
            with(txSession) {
                // ✅ COMPILES! Both Logger and Transaction contexts are active here.
                processPayment(1500.00)
            }
        }

        _uiState.value = "Context Receivers Executed:\n\n" +
                "Injected DefaultLogger & ActiveTransaction into scope using nested 'with' blocks.\n" +
                "processPayment() successfully pulled functions from both interfaces implicitly.\n" +
                "Check Logcat for the execution sequence!"
    }
}