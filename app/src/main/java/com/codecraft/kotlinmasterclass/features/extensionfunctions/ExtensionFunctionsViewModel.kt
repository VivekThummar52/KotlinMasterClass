package com.codecraft.kotlinmasterclass.features.extensionfunctions

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

// Helper classes to demonstrate Static Resolution Behavior
open class ParentDevice
class ChildSmartphone : ParentDevice()

class ConflictDemo {
    fun printMessage() = "Member Function Wins!"
}

@HiltViewModel
class ExtensionFunctionsViewModel @Inject constructor() : ViewModel() {

    private val TAG = "MasterclassLog"

    private val _uiState = MutableStateFlow("Tap an extension example below to see it in action.")
    val uiState: StateFlow<String> = _uiState.asStateFlow()

    // 1. Basic Extension Function Definition
    // Extends the Int class to format numbers cleanly into Indian Currency format (INR)
    private fun Int.toIndianCurrency(): String {
        val locale = Locale("en", "IN")
        val formatter = NumberFormat.getCurrencyInstance(locale)
        return formatter.format(this)
    }

    fun runBasicExtensionDemo() {
        val amount = 150000
        // Calling our extension function just like a regular member function!
        val formatted = amount.toIndianCurrency()

        Log.d(TAG, "Basic Extension: $amount formatted to $formatted")
        _uiState.value = "Basic Extension Executed:\n$amount -> $formatted"
    }

    // 2. Extension Property Definition
    // Adds a read-only property to String to count its total vowels
    private val String.vowelCount: Int
        get() = count { it.lowercaseChar() in listOf('a', 'e', 'i', 'o', 'u') }

    fun runExtensionPropertyDemo() {
        val testString = "Jetpack Compose"
        val count = testString.vowelCount

        Log.d(TAG, "Extension Property: '$testString' has $count vowels")
        _uiState.value = "Extension Property Executed:\nVowels in '$testString' = $count"
    }

    // 3. Nullable Receiver Definition
    // Extension function explicitly defined on a nullable type (CharSequence?)
    private fun CharSequence?.isPerfectNullOrBlank(): Boolean {
        // 'this' refers to the receiver type, which can be null here!
        return this == null || this.trim().isEmpty()
    }

    fun runNullableReceiverDemo() {
        val nullInput: String? = null
        val emptyInput: String = "   "
        val validInput: String = "Kotlin"

        // Notice we don't need safe calls (?.) to execute this extension on a null object!
        val check1 = nullInput.isPerfectNullOrBlank()
        val check2 = emptyInput.isPerfectNullOrBlank()
        val check3 = validInput.isPerfectNullOrBlank()

        Log.d(TAG, "Nullable Receiver outputs: null->$check1, empty->$check2, valid->$check3")
        _uiState.value = "Nullable Receiver Results:\n" +
                "Is null blank? $check1\n" +
                "Is '   ' blank? $check2\n" +
                "Is 'Kotlin' blank? $check3"
    }

    // Helpers to show Polymorphism vs Extension Resolution
    private fun ParentDevice.name() = "Parent Extension"
    private fun ChildSmartphone.name() = "Child Extension"
    
    // Extension matching signature of an existing member function
    private fun ConflictDemo.printMessage() = "Extension Wins?"

    fun runStaticResolutionDemo() {
        // Gotcha A: Extensions are resolved statically based on the reference type, NOT runtime type
        val device: ParentDevice = ChildSmartphone()
        val resolutionResult = device.name() // Will print "Parent Extension" even though it's a Smartphone object!

        // Gotcha B: Member functions always override extension functions if signatures match identically
        val conflict = ConflictDemo()
        val conflictResult = conflict.printMessage() // Will print "Member Function Wins!"

        Log.d(TAG, "Static Resolution Gotchas evaluated.")
        _uiState.value = "Static Resolution Pitfalls:\n\n" +
                "1. Polymorphism Test: Reference type is ParentDevice, object is ChildSmartphone.\n" +
                "Resolved output: '$resolutionResult'\n\n" +
                "2. Member Conflict Test: Signature identical.\n" +
                "Resolved output: '$conflictResult'"
    }
}