package com.example.kotlinmasterclass.features.calculator

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.math.cos
import kotlin.math.sin

@HiltViewModel
class GraphingViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorState())
    val uiState = _uiState.asStateFlow()

    fun appendInput(char: String) {
        _uiState.update { it.copy(expression = it.expression + char) }
    }

    fun backspace() {
        _uiState.update { state ->
            if (state.expression.isNotEmpty()) {
                state.copy(expression = state.expression.dropLast(1))
            } else state
        }
    }

    fun toggleGraphView() {
        _uiState.update { it.copy(isGraphing = !it.isGraphing) }
    }
}