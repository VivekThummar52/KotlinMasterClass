package com.example.kotlinmasterclass.features.audio

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kotlinmasterclass.utils.surfaceAnalysisProvider
import com.example.kotlinmasterclass.ui.components.MasterclassTopAppBar
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.ui.tooling.preview.Preview
import com.example.kotlinmasterclass.ui.theme.KotlinMasterclassTheme
import androidx.hilt.navigation.compose.hiltViewModel

val DarkAudioColors = AudioColors(
    bg = Color(0xFF0F0F13), vinyl = Color(0xFF1A1A24), groove = Color(0xFF2A2A35),
    brand = Color(0xFFFFD700), neon = Color(0xFFB388FF), active = Color(0xFF18FFFF),
    inactive = Color(0xFF5A5A65), textMain = Color.White, textMuted = Color.Gray
)

val LightAudioColors = AudioColors(
    bg = Color(0xFFF3F4F6), vinyl = Color(0xFFE5E7EB), groove = Color(0xFFD1D5DB),
    brand = Color(0xFFF59E0B), neon = Color(0xFF7C3AED), active = Color(0xFF0284C7),
    inactive = Color(0xFF9CA3AF), textMain = Color(0xFF111827), textMuted = Color(0xFF6B7280)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioStudioScreen(
    viewModel: AudioStudioViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val haptic = LocalHapticFeedback.current

    // THE FIX: Listen to the active MaterialTheme token provider directly instead of the system OS state
    val isDarkTheme = !MaterialTheme.colorScheme.surfaceAnalysisProvider()
    val colors = if (isDarkTheme) DarkAudioColors else LightAudioColors

    Scaffold(
        topBar = {
            MasterclassTopAppBar(
                title = { Text("Audio Visualizer Studio", color = colors.textMain) },
                onBackClick = onBackClick,
                onSettingsClick = onSettingsClick,
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.bg, navigationIconContentColor = colors.textMain, actionIconContentColor = colors.textMain)
            )
        },
        containerColor = colors.bg
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues), horizontalAlignment = Alignment.CenterHorizontally) {

            // 1. RADIAL VISUALIZER
            Box(modifier = Modifier.fillMaxWidth().weight(1f).padding(32.dp), contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val center = Offset(size.width / 2, size.height / 2)
                    val baseRadius = size.width * 0.25f
                    val pulseRadius = baseRadius + (state.beatPulse * 20f)
                    val bandCount = state.frequencies.size
                    val angleStep = 360f / bandCount

                    for (i in 0 until bandCount) {
                        val angle = (i * angleStep) - 90f
                        val angleRad = Math.toRadians(angle.toDouble())
                        val amplitude = state.frequencies[i] * 120f
                        val startRadius = pulseRadius + 20f
                        val endRadius = startRadius + amplitude

                        val startX = center.x + (cos(angleRad) * startRadius).toFloat()
                        val startY = center.y + (sin(angleRad) * startRadius).toFloat()
                        val endX = center.x + (cos(angleRad) * endRadius).toFloat()
                        val endY = center.y + (sin(angleRad) * endRadius).toFloat()

                        val bandColor = androidx.compose.ui.graphics.lerp(colors.brand, colors.active, i / bandCount.toFloat())
                        drawLine(color = bandColor, start = Offset(startX, startY), end = Offset(endX, endY), strokeWidth = 12f, cap = StrokeCap.Round)
                    }

                    rotate(degrees = state.vinylRotation, pivot = center) {
                        drawCircle(color = colors.vinyl, radius = pulseRadius, center = center)
                        drawCircle(color = colors.groove, radius = pulseRadius * 0.8f, center = center, style = Stroke(width = 2f))
                        drawCircle(color = colors.groove, radius = pulseRadius * 0.6f, center = center, style = Stroke(width = 2f))
                        drawCircle(color = colors.groove, radius = pulseRadius * 0.4f, center = center, style = Stroke(width = 2f))
                        drawCircle(brush = Brush.linearGradient(listOf(colors.brand, colors.neon)), radius = pulseRadius * 0.25f, center = center)
                        drawCircle(color = colors.bg, radius = pulseRadius * 0.05f, center = center)
                    }
                }
            }

            // 2. TRACK INFO & CONTROLS
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("NEON OVERDRIVE MIX", color = colors.textMain, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                    Text("Synthwave Cartel", color = colors.textMuted, style = MaterialTheme.typography.labelLarge)
                }
                val heartScale by animateFloatAsState(targetValue = if (state.isLiked) 1.2f else 1f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow), label = "")
                IconButton(onClick = { haptic.performHapticFeedback(HapticFeedbackType.LongPress); viewModel.toggleLike() }, modifier = Modifier.scale(heartScale)) {
                    Icon(if (state.isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder, contentDescription = null, tint = if (state.isLiked) colors.brand else colors.textMain)
                }
                IconButton(onClick = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove) }) {
                    Icon(Icons.Outlined.Info, contentDescription = null, tint = colors.textMain)
                }
            }

            // 3. TACTILE WAVEFORM
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp, vertical = 24.dp)) {
                var scrubAnchor by remember { mutableFloatStateOf(0f) }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(24.dp)) // THE FIX: Parent acts as a perfect mask
                        .background(colors.vinyl)        // Background applied after clipping
                        .pointerInput(Unit) {
                            detectHorizontalDragGestures(
                                onDragStart = { scrubAnchor = state.trackProgress },
                                onHorizontalDrag = { change, dragAmount ->
                                    change.consume()
                                    val newProgress = (state.trackProgress + (dragAmount / size.width)).coerceIn(0f, 1f)
                                    viewModel.updateProgress(newProgress)
                                    if (Math.abs(newProgress - scrubAnchor) > 0.02f) {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        scrubAnchor = newProgress
                                    }
                                }
                            )
                        }
                ) {
                    // The inner fill is now just a plain rectangle
                    // It won't glitch at 1% because the parent handles the rounded corners!
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(state.trackProgress)
                            .background(Brush.horizontalGradient(listOf(colors.neon, colors.active)))
                    )
                }
            }

            // 4. TRANSPORT DECK
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).padding(bottom = 48.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); viewModel.toggleShuffle() }) {
                    Icon(Icons.Filled.Shuffle, contentDescription = null, tint = if (state.isShuffleOn) colors.active else colors.inactive)
                }
                IconButton(onClick = { haptic.performHapticFeedback(HapticFeedbackType.LongPress) }) {
                    Icon(Icons.Filled.FastRewind, contentDescription = null, tint = colors.textMain, modifier = Modifier.size(36.dp))
                }
                FloatingActionButton(onClick = { haptic.performHapticFeedback(HapticFeedbackType.LongPress); viewModel.togglePlayback() }, containerColor = colors.brand, shape = CircleShape, modifier = Modifier.size(80.dp)) {
                    Icon(if (state.isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
                }
                IconButton(onClick = { haptic.performHapticFeedback(HapticFeedbackType.LongPress) }) {
                    Icon(Icons.Filled.FastForward, contentDescription = null, tint = colors.textMain, modifier = Modifier.size(36.dp))
                }
                IconButton(onClick = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); viewModel.toggleRepeat() }) {
                    val repeatIcon = if (state.repeatMode.name == "ONE") Icons.Filled.RepeatOne else Icons.Filled.Repeat
                    val repeatTint = if (state.repeatMode.name == "OFF") colors.inactive else colors.active
                    Icon(repeatIcon, contentDescription = null, tint = repeatTint)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AudioStudioScreenPreview() {
    KotlinMasterclassTheme {
        AudioStudioScreen(
            onBackClick = {},
            onSettingsClick = {}
        )
    }
}
