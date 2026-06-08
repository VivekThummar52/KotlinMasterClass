package com.example.kotlinmasterclass.features.contracts

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

// A sample domain model
data class UserProfile(val id: String, val name: String, val email: String?)

@OptIn(ExperimentalContracts::class)
@HiltViewModel
class ContractsViewModel @Inject constructor() : ViewModel() {

    private val TAG = "MasterclassLog"

    private val _uiState = MutableStateFlow("Tap a Contract evaluation below.")
    val uiState: StateFlow<String> = _uiState.asStateFlow()

    /**
     * 1. The "Returns Implies" Contract
     * Tells the compiler: "If this function returns true, the object is absolutely NOT NULL."
     */
    private fun isProfileValid(profile: UserProfile?): Boolean {
        contract {
            returns(true) implies (profile != null)
        }
        return profile != null && profile.name.isNotBlank() && profile.id.isNotEmpty()
    }

    fun runSmartCastDemo() {
        val unknownProfile: UserProfile? = UserProfile("U123", "Vivek", "vivek@example.com")

        // Without the contract, unknownProfile.name would trigger a compile error here.
        // Because isProfileValid returned true, the compiler SMART CASTS it to non-null automatically!
        if (isProfileValid(unknownProfile)) {
            val nameLength = unknownProfile.name.length // Notice: No safe call (?.) or bang (!!) needed!
            
            Log.d(TAG, "Smart Cast Success: ${unknownProfile.name} is valid.")
            _uiState.value = "Returns Implies Contract:\n" +
                    "Evaluated unknownProfile.\n" +
                    "Compiler automatically smart-casted it to non-null!\n" +
                    "Name Length: $nameLength"
        } else {
            _uiState.value = "Profile was invalid."
        }
    }

    /**
     * 2. The "Calls In Place" Contract
     * Tells the compiler: "This lambda will be executed EXACTLY ONCE."
     * This allows us to initialize `val` variables inside a lambda safely.
     */
    private inline fun measureExecution(block: () -> Unit) {
        contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
        }
        val start = System.currentTimeMillis()
        block()
        val end = System.currentTimeMillis()
        Log.d(TAG, "Execution took ${end - start}ms")
    }

    fun runCallsInPlaceDemo() {
        val computationResult: String // Notice this is a 'val', not a 'var', and it is uninitialized!

        // Because of EXACTLY_ONCE, the compiler knows computationResult will be assigned once and only once.
        // Without the contract, this would throw a "Captured values initialization is forbidden" compile error.
        measureExecution {
            Thread.sleep(100) // Simulate work
            computationResult = "Initialization Successful"
        }

        _uiState.value = "Calls In Place Contract:\n" +
                "Successfully initialized a 'val' inside a lambda.\n" +
                "Result: $computationResult"
    }
}