package com.example.kotlinmasterclass.features.testing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestingViewModel @Inject constructor() : ViewModel() {

    private val _realTimeLog = MutableStateFlow(listOf<String>())
    val realTimeLog = _realTimeLog.asStateFlow()

    private val _virtualTimeLog = MutableStateFlow(listOf<String>())
    val virtualTimeLog = _virtualTimeLog.asStateFlow()

    fun runRealTimeSimulation() {
        viewModelScope.launch {
            _realTimeLog.value = listOf("Started Real Time...")
            val startTime = System.currentTimeMillis()
            
            delay(3000) // Actually waits 3 seconds
            _realTimeLog.value = _realTimeLog.value + "Process 1 Complete"
            
            delay(3000) // Actually waits 3 seconds
            val totalTime = System.currentTimeMillis() - startTime
            _realTimeLog.value = _realTimeLog.value + "Finished in ${totalTime}ms"
        }
    }

    fun runVirtualTimeSimulation() {
        // In a real UI, we can't use runTest (it's JVM only). 
        // So we simulate the instant result here for educational purposes.
        viewModelScope.launch {
            _virtualTimeLog.value = listOf("Started Virtual Time...")
            val startTime = System.currentTimeMillis()
            
            // Virtual Time Schedulers skip delays instantly
            _virtualTimeLog.value = _virtualTimeLog.value + "Process 1 Complete (Skipped 3000ms delay)"
            _virtualTimeLog.value = _virtualTimeLog.value + "Finished in ${System.currentTimeMillis() - startTime}ms"
        }
    }
}