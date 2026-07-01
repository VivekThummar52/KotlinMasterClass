package com.codecraft.kotlinmasterclass.features.myhealthvitals

import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class HealthMetric(
    val label: String,
    val value: String,
    val icon: ImageVector,
    val iconColor: Color,
    val graphType: GraphType
)

enum class GraphType {
    WAVY, BARS
}

data class HealthVitalsState(
    val heartRate: HealthMetric,
    val sleep: HealthMetric,
    val steps: HealthMetric,
    val energy: HealthMetric
)

@HiltViewModel
class MyHealthViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(
        HealthVitalsState(
            heartRate = HealthMetric("Heart Rate", "82", androidx.compose.material.icons.Icons.Default.Favorite, Color(0xFF5E46F1), GraphType.WAVY),
            sleep = HealthMetric("Sleep", "6.5h", androidx.compose.material.icons.Icons.Default.NightsStay, Color(0xFF5E46F1), GraphType.BARS),
            steps = HealthMetric("Steps", "7k", androidx.compose.material.icons.Icons.AutoMirrored.Filled.DirectionsWalk, Color(0xFF4CAF50), GraphType.BARS),
            energy = HealthMetric("Energy", "78%", androidx.compose.material.icons.Icons.Default.Bolt, Color(0xFFFF9800), GraphType.BARS)
        )
    )
    val uiState: StateFlow<HealthVitalsState> = _uiState.asStateFlow()
}
