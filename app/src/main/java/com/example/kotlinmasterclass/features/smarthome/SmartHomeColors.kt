package com.example.kotlinmasterclass.features.smarthome

import androidx.compose.ui.graphics.Color

// --- THEME STATE ---
data class SmartHomeColors(
    val bg: Color,
    val lightShadow: Color,
    val darkShadow: Color,
    val accent: Color,
    val textMain: Color,
    val textMuted: Color
)

// --- DOMAIN MODELS ---
data class SmartDevice(
    val id: String,
    val name: String,
    val iconName: String,
    val isActive: Boolean = false
)

data class SmartHomeState(
    val temperature: Float = 22f,
    val activeRoomId: String = "Living Room",
    val devices: List<SmartDevice> = listOf(
        SmartDevice("1", "Main Lights", "Light", true),
        SmartDevice("2", "Thermostat AC", "AC", false),
        SmartDevice("3", "OLED TV", "TV", true),
        SmartDevice("4", "Sound System", "Speaker", false)
    )
)