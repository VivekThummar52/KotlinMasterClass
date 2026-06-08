package com.example.kotlinmasterclass.features.higherorderfunctions

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class HigherOrderFunctionsViewModel @Inject constructor() : ViewModel() {

    private val TAG = "MasterclassLog"

    private val _uiState = MutableStateFlow("Tap a higher-order feature below to explore execution logic.")
    val uiState: StateFlow<String> = _uiState.asStateFlow()

    // 1. Passing a Function as an Argument
    // This is a Higher-Order Function because it accepts a function execution definition as a parameter
    private fun calculateResult(a: Int, b: Int, operation: (Int, Int) -> Int): Int {
        return operation(a, b)
    }

    fun runPassFunctionDemo() {
        // We pass the numbers along with an explicit lambda definition block
        val sum = calculateResult(12, 5) { x, y -> x + y }
        val product = calculateResult(12, 5) { x, y -> x * y }

        Log.d(TAG, "Higher-Order Input: Sum=$sum, Product=$product")
        _uiState.value = "Function Arguments Executed:\nSum: 12 + 5 = $sum\nProduct: 12 * 5 = $product"
    }

    // 2. Returning a Function (Function Factory Pattern)
    // This function returns another function of type (String) -> String
    private fun provideStringFormatter(prefix: String): (String) -> String {
        return { inputMessage -> "[$prefix] $inputMessage" }
    }

    fun runReturnFunctionDemo() {
        // Construct two distinct operational functions via our factory pattern
        val logFormatter = provideStringFormatter("LOG")
        val errorFormatter = provideStringFormatter("CRITICAL ERROR")

        // Execute the returned manufactured functions
        val logOutput = logFormatter("Battery configuration updated successfully.")
        val errorOutput = errorFormatter("Database validation handshake failed! (Static message)")

        Log.d(TAG, "Factory outputs generated.")
        _uiState.value = "Function Factory Executed:\n$logOutput\n$errorOutput"
    }

    // 3. Inline Optimization Demonstration
    // The 'inline' keyword instructs the compiler to copy the function bytecode directly 
    // into the call site, eliminating memory allocation overhead for object lambdas.
    private inline fun executeOptimizedBlock(tag: String, action: () -> Unit) {
        Log.d(TAG, "--- Inline Execution Start: $tag ---")
        action()
        Log.d(TAG, "--- Inline Execution End ---")
    }

    // 'noinline' lets you pick which lambdas are skipped during code compiler expansion
    // 'crossinline' guarantees that non-local returns (exiting parent scopes early) cannot happen
    private inline fun handleMixedCallbacks(
        crossinline secureAction: () -> Unit,
        noinline skippedAction: () -> Unit
    ) {
        executeOptimizedBlock("SECURE") {
            secureAction() // Allowed inside nested blocks because it's marked crossinline
        }
        skippedAction() // Preserved as an actual function object behind the scenes
    }

    fun runInlineKeywordsDemo() {
        Log.d(TAG, "Starting Inline optimization simulation.")
        
        handleMixedCallbacks(
            secureAction = { Log.d(TAG, "Executing crossinline callback safely.") },
            skippedAction = { Log.d(TAG, "Executing noinline callback parameter object.") }
        )

        _uiState.value = "Inline Keywords Executed.\n\nCheck your project bytecode layout mentally: " +
                "The function code body was completely unrolled directly into this method execution frame! " +
                "Check Logcat for sequence order results."
    }
}