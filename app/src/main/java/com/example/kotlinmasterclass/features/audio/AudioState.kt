package com.example.kotlinmasterclass.features.audio

data class AudioState(
    val isPlaying: Boolean = false,
    val frequencies: List<Float> = List(64) { 0f },
    val beatPulse: Float = 0f,
    val trackProgress: Float = 0.3f,
    val vinylRotation: Float = 0f,
    // NEW STATES
    val isLiked: Boolean = false,
    val isShuffleOn: Boolean = false,
    val repeatMode: RepeatMode = RepeatMode.OFF
)