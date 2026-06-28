package com.example.kotlinmasterclass.features.sealedclasses

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// 1. Enum Definition: Bounded, uniform constants (No unique data states per item)
enum class BatteryStatus {
    GOOD, OVERHEATING, DEAD
}

// 2. Sealed Class Definition: Bounded hierarchy, but subclasses can hold varying unique data shapes
sealed class TransactionState {
    object Idle : TransactionState()
    object Loading : TransactionState()
    data class Success(val receiptId: String, val amount: Double) : TransactionState()
    data class Failure(val errorCode: Int, val errorMessage: String) : TransactionState()
}

@HiltViewModel
class SealedClassesViewModel @Inject constructor() : ViewModel() {

    private val TAG = "MasterclassLog"

    private val _uiState = MutableStateFlow("Tap an option below to simulate state transformations.")
    val uiState: StateFlow<String> = _uiState.asStateFlow()

    /**
     * Demonstrates how Enums represent static, predefined states uniformly.
     */
    fun runEnumDemo() {
        val currentStatus = BatteryStatus.entries.random()

        // Enums are great for simple evaluation blocks
        val recommendation = when (currentStatus) {
            BatteryStatus.GOOD -> "All systems normal."
            BatteryStatus.OVERHEATING -> "(Warning) Cooling cycle initiated!"
            BatteryStatus.DEAD -> "Alert: Replace hardware immediately."
        }

        Log.d(TAG, "Enum Evaluation: Status is $currentStatus -> $recommendation")
        _uiState.value = "Enum Evaluation:\nStatus: $currentStatus\nAction: $recommendation"
    }

    /**
     * Demonstrates how Sealed Classes allow highly distinct data states per branch.
     */
    fun runSealedSuccessDemo() {
        // Simulating a successful state payload containing unique transmission tokens
        val successfulState: TransactionState = TransactionState.Success(receiptId = "TXN-94821", amount = 1450.50)
        evaluateTransaction(successfulState)
    }

    fun runSealedFailureDemo() {
        // Simulating a failure payload carrying context-specific diagnostics
        val errorState: TransactionState = TransactionState.Failure(errorCode = 404, errorMessage = "Gateway Timeout")
        evaluateTransaction(errorState)
    }

    private fun evaluateTransaction(state: TransactionState) {
        // The power of 'when' with sealed classes: The compiler enforces exhaustiveness.
        // No 'else' block is required because the compiler checks every subclass branch at compile time!
        val displayMessage = when (state) {
            is TransactionState.Idle -> "System is waiting for input parameters."
            is TransactionState.Loading -> "Processing transaction payload with server..."
            is TransactionState.Success -> "Payment Cleared! ID: ${state.receiptId} for ₹${state.amount}"
            is TransactionState.Failure -> "Transaction Rejected [Error ${state.errorCode}]: ${state.errorMessage}"
        }

        Log.d(TAG, "Sealed State Processing: $displayMessage")
        _uiState.value = "Sealed Class Evaluation:\n\n$displayMessage"
    }
}