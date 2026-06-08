package com.example.kotlinmasterclass.utils

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

// Determines if the dynamic parent Material3Theme context wrapper is light or dark
@Composable
internal fun ColorScheme.surfaceAnalysisProvider(): Boolean {
    // If the surface luminance color profile is dark, it returns false
    return this.surface.red > 0.5f && this.surface.green > 0.5f && this.surface.blue > 0.5f
}