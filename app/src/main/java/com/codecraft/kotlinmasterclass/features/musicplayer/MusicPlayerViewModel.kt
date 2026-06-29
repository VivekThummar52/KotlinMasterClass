package com.codecraft.kotlinmasterclass.features.musicplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicPlayerViewModel @Inject constructor() : ViewModel() {

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentPosition = MutableStateFlow(0f)
    val currentPosition: StateFlow<Float> = _currentPosition.asStateFlow()

    val trackDuration = 230f // 3:50

    // Expanded Mock Data
    val trackTitle = "Starboy"
    val trackArtist = "The Weeknd ft. Daft Punk"
    val albumName = "Starboy"
    val releaseDate = "Nov 25, 2016"
    val likes = "8.2M"
    val dislikes = "45K"

    private var playbackJob: Job? = null

    fun togglePlayPause() {
        _isPlaying.value = !_isPlaying.value
        if (_isPlaying.value) {
            startPlayback()
        } else {
            playbackJob?.cancel()
        }
    }

    fun seekTo(position: Float) {
        _currentPosition.value = position
        if (_currentPosition.value >= trackDuration) {
            _currentPosition.value = trackDuration
            _isPlaying.value = false
            playbackJob?.cancel()
        }
    }

    private fun startPlayback() {
        playbackJob?.cancel()
        playbackJob = viewModelScope.launch {
            while (isActive && _currentPosition.value < trackDuration) {
                delay(1000)
                _currentPosition.value += 1f
            }
            if (_currentPosition.value >= trackDuration) {
                _isPlaying.value = false
            }
        }
    }

    fun formatTimestamp(seconds: Float): String {
        val totalSeconds = seconds.toInt()
        val m = totalSeconds / 60
        val s = totalSeconds % 60
        return String.format("%d:%02d", m, s)
    }
}