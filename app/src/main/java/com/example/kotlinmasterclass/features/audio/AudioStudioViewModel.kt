package com.example.kotlinmasterclass.features.audio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.sin

@HiltViewModel
class AudioStudioViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(AudioState())
    val uiState = _uiState.asStateFlow()

    private var time = 0f

    init {
        startAudioEngine()
    }

    // --- TRANSPORT TOGGLES ---
    fun togglePlayback() = _uiState.update { state ->
        val willPlay = !state.isPlaying
        // UX Enhancement: If we are hitting play and the track is finished, automatically restart it
        val nextProgress = if (willPlay && state.trackProgress >= 1f) 0f else state.trackProgress
        state.copy(isPlaying = willPlay, trackProgress = nextProgress)
    }

    fun updateProgress(progress: Float) = _uiState.update { it.copy(trackProgress = progress.coerceIn(0f, 1f)) }
    fun toggleLike() = _uiState.update { it.copy(isLiked = !it.isLiked) }
    fun toggleShuffle() = _uiState.update { it.copy(isShuffleOn = !it.isShuffleOn) }

    fun toggleRepeat() {
        _uiState.update {
            val nextMode = when (it.repeatMode.name) {
                "OFF" -> RepeatMode.ALL
                "ALL" -> RepeatMode.ONE
                else -> RepeatMode.OFF
            }
            it.copy(repeatMode = nextMode)
        }
    }

    // --- FFT ENGINE ---
    private fun startAudioEngine() {
        viewModelScope.launch {
            while (true) {
                delay(16) // ~60 FPS
                val currentState = _uiState.value

                if (currentState.isPlaying) {
                    time += 0.05f
                    val newFrequencies = List(64) { index ->
                        val base = sin(time * 2f + index * 0.1f) * 0.5f + 0.5f
                        val harmonic = sin(time * 4f - index * 0.2f) * 0.3f
                        val noise = (Math.random().toFloat() * 0.2f)
                        val bassBoost = if (index < 10) 1.5f else 1.0f
                        ((base + harmonic + noise) * bassBoost).coerceIn(0.1f, 1f)
                    }

                    val bassAverage = newFrequencies.take(5).average().toFloat()
                    val newPulse = if (bassAverage > 0.85f) 1f else (currentState.beatPulse - 0.05f).coerceAtLeast(0f)

                    _uiState.update { state ->
                        val nextProgress = state.trackProgress + 0.0005f

                        // THE FIX: If Repeat is ALL or ONE, restart the track. Otherwise, pause at 100%.
                        val (finalProgress, finalIsPlaying) = if (nextProgress >= 1f) {
                            if (state.repeatMode.name == "ONE" || state.repeatMode.name == "ALL") {
                                0f to true // Restart the song
                            } else {
                                1f to false // Stop progress and pause the player
                            }
                        } else {
                            nextProgress to state.isPlaying // Continue normally
                        }

                        state.copy(
                            isPlaying = finalIsPlaying,
                            frequencies = newFrequencies,
                            beatPulse = newPulse,
                            vinylRotation = (state.vinylRotation + 1.5f) % 360f,
                            trackProgress = finalProgress
                        )
                    }
                } else {
                    // Decay the FFT visualizer smoothly when paused
                    val decayedFreqs = currentState.frequencies.map { (it - 0.05f).coerceAtLeast(0.05f) }
                    _uiState.update {
                        it.copy(
                            frequencies = decayedFreqs,
                            beatPulse = (it.beatPulse - 0.05f).coerceAtLeast(0f)
                        )
                    }
                }
            }
        }
    }
}