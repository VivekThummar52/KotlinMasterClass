package com.example.kotlinmasterclass.features.calculator

import androidx.compose.ui.graphics.Color

// --- THEME STATE ---
data class CalcColors(
    val bg: Color,
    val panel: Color,
    val accent: Color,
    val textMain: Color,
    val padGray: Color
)

// --- DOMAIN MODELS ---
data class CalculatorState(
    val expression: String = "",
    val isGraphing: Boolean = false
)