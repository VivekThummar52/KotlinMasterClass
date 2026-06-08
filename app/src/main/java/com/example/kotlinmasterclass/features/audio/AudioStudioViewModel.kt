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
    fun togglePlayback() = _uiState.update { it.copy(isPlaying = !it.isPlaying) }
    fun updateProgress(progress: Float) = _uiState.update { it.copy(trackProgress = progress.coerceIn(0f, 1f)) }
    fun toggleLike() = _uiState.update { it.copy(isLiked = !it.isLiked) }
    fun toggleShuffle() = _uiState.update { it.copy(isShuffleOn = !it.isShuffleOn) }

    fun toggleRepeat() {
        _uiState.update {
            val nextMode = when (it.repeatMode) {
                RepeatMode.OFF -> RepeatMode.ALL
                RepeatMode.ALL -> RepeatMode.ONE
                RepeatMode.ONE -> RepeatMode.OFF
            }
            it.copy(repeatMode = nextMode)
        }
    }

    // --- FFT ENGINE ---
    private fun startAudioEngine() {
        viewModelScope.launch {
            while (true) {
                delay(16)
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

                    _uiState.update {
                        it.copy(
                            frequencies = newFrequencies,
                            beatPulse = newPulse,
                            vinylRotation = (it.vinylRotation + 1.5f) % 360f,
                            // If repeat ONE is on, loop progress back to 0 when it hits 1
                            trackProgress = if (it.trackProgress >= 0.999f && it.repeatMode == RepeatMode.ONE) 0f
                            else (it.trackProgress + 0.0005f).coerceIn(0f, 1f)
                        )
                    }
                } else {
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