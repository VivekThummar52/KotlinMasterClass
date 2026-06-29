package com.codecraft.kotlinmasterclass.features.smarthome

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Speaker
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codecraft.kotlinmasterclass.ui.components.MasterclassTopAppBar
import androidx.compose.ui.tooling.preview.Preview
import com.codecraft.kotlinmasterclass.ui.theme.KotlinMasterclassTheme
import androidx.hilt.navigation.compose.hiltViewModel
import com.codecraft.kotlinmasterclass.utils.surfaceAnalysisProvider
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

// --- NEUMORPHIC PALETTES ---
val DarkNeoColors = SmartHomeColors(
    bg = Color(0xFF292D32), lightShadow = Color(0xFF383D44), darkShadow = Color(0xFF1A1D20),
    accent = Color(0xFF00E5FF), textMain = Color.White, textMuted = Color(0xFF8E9BAE)
)

val LightNeoColors = SmartHomeColors(
    bg = Color(0xFFE0E5EC), lightShadow = Color(0xFFFFFFFF), darkShadow = Color(0xFFA3B1C6),
    accent = Color(0xFF2962FF), textMain = Color(0xFF292D32), textMuted = Color(0xFF78909C)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartHomeScreen(
    viewModel: SmartHomeViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val isDarkTheme = !MaterialTheme.colorScheme.surfaceAnalysisProvider()
    val colors = if (isDarkTheme) DarkNeoColors else LightNeoColors

    Scaffold(
        topBar = {
            MasterclassTopAppBar(
                title = { Text("Smart Hub", color = colors.textMain, fontWeight = FontWeight.Bold) },
                onBackClick = onBackClick, onSettingsClick = onSettingsClick,
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.bg, navigationIconContentColor = colors.textMain, actionIconContentColor = colors.textMain)
            )
        },
        containerColor = colors.bg
    ) { paddingValues ->
        // THE FIX: Removed .padding(24.dp) from this parent Column
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 1. Room Selector
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth(),
                // THE FIX: Move the padding to contentPadding so shadows can safely bleed into the safe-zone edges
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 20.dp)
            ) {
                items(listOf("Living Room", "Bedroom", "Kitchen", "Office")) { room ->
                    val isSelected = state.activeRoomId == room
                    Box(
                        modifier = Modifier
                            .neumorphic(colors, isPressed = isSelected, cornerRadius = 24.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .clickable { viewModel.selectRoom(room) }
                            .padding(horizontal = 24.dp, vertical = 12.dp)
                    ) {
                        Text(room, color = if (isSelected) colors.accent else colors.textMuted, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 2. The Neumorphic Thermostat Dial
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .neumorphic(colors, isPressed = false, cornerRadius = 140.dp)
                    .padding(32.dp)
                    .neumorphic(colors, isPressed = true, cornerRadius = 108.dp) // Inner pressed ring
                    .pointerInput(Unit) {
                        detectDragGestures { change, _ ->
                            change.consume()
                            // Calculate angle from center of the dial
                            val centerX = size.width / 2f
                            val centerY = size.height / 2f
                            val angleRad = atan2(change.position.y - centerY, change.position.x - centerX)
                            var angleDeg = Math.toDegrees(angleRad.toDouble()).toFloat() + 90f
                            if (angleDeg < 0) angleDeg += 360f

                            // Map 0-360 degrees to 16-32 Celsius
                            val newTemp = 16f + (angleDeg / 360f) * 16f
                            viewModel.setTemperature(newTemp)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                // Dial Indicator Dot
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val tempProgress = (state.temperature - 16f) / 16f
                    val angleRad = Math.toRadians((tempProgress * 360f - 90f).toDouble())
                    val radius = size.width / 2.2f
                    val x = size.width / 2 + (cos(angleRad) * radius).toFloat()
                    val y = size.height / 2 + (sin(angleRad) * radius).toFloat()
                    drawCircle(color = colors.accent, radius = 12f, center = Offset(x, y))
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${state.temperature.toInt()}°", color = colors.textMain, fontSize = 64.sp, fontWeight = FontWeight.Black)
                    Text("CELSIUS", color = colors.textMuted, letterSpacing = 2.sp, style = MaterialTheme.typography.labelMedium)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 3. Quick Device Toggles
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                // THE FIX: Added contentPadding to prevent top/bottom shadow clipping, and added weight(1f) to ensure scroll space
                contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 32.dp),
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                items(state.devices) { device ->
                    val icon = when(device.iconName) {
                        "Light" -> Icons.Filled.Lightbulb
                        "AC" -> Icons.Filled.AcUnit
                        "TV" -> Icons.Filled.Tv
                        else -> Icons.Filled.Speaker
                    }

                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .neumorphic(colors, isPressed = device.isActive, cornerRadius = 32.dp)
                            .clip(RoundedCornerShape(32.dp))
                            .clickable { viewModel.toggleDevice(device.id) }
                            .padding(20.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                            Icon(icon, contentDescription = null, tint = if (device.isActive) colors.accent else colors.textMuted, modifier = Modifier.size(36.dp))
                            Text(device.name, color = if (device.isActive) colors.textMain else colors.textMuted, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SmartHomeScreenPreview() {
    KotlinMasterclassTheme {
        SmartHomeScreen(
            onBackClick = {},
            onSettingsClick = {}
        )
    }
}

// --- THE MASTERCLASS NEUMORPHIC SHADER ---
// --- THE UPGRADED NEUMORPHIC SHADER (Backward Compatible) ---
fun Modifier.neumorphic(
    colors: SmartHomeColors,
    isPressed: Boolean = false,
    cornerRadius: androidx.compose.ui.unit.Dp = 16.dp,
    blurRadius: Float = 20f
) = this.drawBehind {
    drawIntoCanvas { canvas ->
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.isAntiAlias = true

        // THE FIX: Use BlurMaskFilter instead of setShadowLayer.
        // This is strictly supported by Hardware Acceleration on Android 8 and below.
        frameworkPaint.maskFilter = BlurMaskFilter(
            blurRadius,
            BlurMaskFilter.Blur.NORMAL
        )

        val offset = if (isPressed) -10f else 10f

        // 1. Draw the Dark Shadow (Bottom Right)
        frameworkPaint.color = colors.darkShadow.toArgb()
        canvas.save()
        canvas.translate(offset, offset)
        canvas.drawRoundRect(0f, 0f, size.width, size.height, cornerRadius.toPx(), cornerRadius.toPx(), paint)
        canvas.restore()

        // 2. Draw the Light Shadow (Top Left)
        frameworkPaint.color = colors.lightShadow.toArgb()
        canvas.save()
        canvas.translate(-offset, -offset)
        canvas.drawRoundRect(0f, 0f, size.width, size.height, cornerRadius.toPx(), cornerRadius.toPx(), paint)
        canvas.restore()
    }
}.background(colors.bg, androidx.compose.foundation.shape.RoundedCornerShape(cornerRadius))