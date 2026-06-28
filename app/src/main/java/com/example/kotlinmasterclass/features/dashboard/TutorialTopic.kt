package com.example.kotlinmasterclass.features.dashboard

import androidx.compose.ui.graphics.Color

data class TutorialTopic(
    val title: String,
    val description: String,
    val containerColor: Color,
    val onClick: () -> Unit
)