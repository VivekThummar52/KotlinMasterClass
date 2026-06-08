package com.example.kotlinmasterclass.features.smartcity

import androidx.compose.ui.graphics.Color

data class CityBuilding(
    val id: Int, 
    val x: Float, 
    val y: Float, 
    val w: Float, 
    val h: Float,
    val heightFactor: Float, 
    val isPowerHub: Boolean = false,
    val hasIncident: Boolean = false
)

data class PowerLine(val startId: Int, val endId: Int)

data class CityState(
    val powerLoad: Float = 0f,
    val incidents: Int = 0,
    val energyPhase: Float = 0f,
    val buildings: List<CityBuilding> = emptyList(),
    val network: List<PowerLine> = emptyList()
)

data class CityColors(
    val bg: Color, 
    val grid: Color, 
    val wall: Color, 
    val roof: Color,
    val energy: Color, 
    val hub: Color, 
    val alert: Color, 
    val textMain: Color
)

val DarkCityColors = CityColors(
    bg = Color(0xFF04060F), grid = Color(0xFF0F1A2C), wall = Color(0xFF16253D),
    roof = Color(0xFF1E3250), energy = Color(0xFF00E5FF), hub = Color(0xFFD500F9),
    alert = Color(0xFFFF1744), textMain = Color.White
)

val LightCityColors = CityColors(
    bg = Color(0xFFF8FAFC), grid = Color(0xFFE2E8F0), wall = Color(0xFFCBD5E1),
    roof = Color(0xFF94A3B8), energy = Color(0xFF0284C7), hub = Color(0xFF9333EA),
    alert = Color(0xFFDC2626), textMain = Color(0xFF0F172A)
)