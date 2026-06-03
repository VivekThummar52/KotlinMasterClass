package com.example.kotlinmasterclass.features.canvas

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CanvasScreen(
    viewModel: CanvasViewModel,
    onBackClick: () -> Unit
) {
    val batteryLevel by viewModel.batteryLevel.collectAsState()
    val isCharging by viewModel.isCharging.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Canvas & Animations") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            // 1. ALIGNMENT FIX: Anchors the content to the bottom of the screen
            verticalArrangement = Arrangement.Bottom
        ) {

            // Adds ergonomic spacing at the top to push the battery down for thumb reachability
            Spacer(modifier = Modifier.height(64.dp))

            // The Reusable Canvas Component
            AnimatedBatteryGauge(
                level = batteryLevel,
                isCharging = isCharging,
                modifier = Modifier
                    .size(width = 200.dp, height = 300.dp)
                    .padding(32.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Sandbox Controls
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Live Hardware Simulator", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Battery Level: ${batteryLevel.toInt()}%")
                    Slider(
                        value = batteryLevel,
                        onValueChange = { viewModel.setBatteryLevel(it) },
                        valueRange = 0f..100f,
                        // 2. SLIDER FIX: Explicitly sets the remaining progress track to be visible in both themes
                        colors = SliderDefaults.colors(
                            inactiveTrackColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.toggleCharging() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (batteryLevel >= 100) {
                            Text(if (isCharging) "Unplug Charger" else "Battery charged")
                        } else {
                            Text(if (isCharging) "Unplug Charger" else "Plug In Charger")
                        }
                    }
                }
            }
        }
    }
}

/**
 * A highly modular component ready to be dropped into any production application.
 */
@Composable
fun AnimatedBatteryGauge(
    level: Float,
    isCharging: Boolean,
    modifier: Modifier = Modifier
) {
    // Smoothly animate the fluid level changing
    val animatedLevel by animateFloatAsState(
        targetValue = level,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "fluid_level"
    )

    // Create a pulsing glow effect when charging
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    // Determine colors based on battery health
    val fluidColor = when {
        level <= 20f -> Color(0xFFEF4444) // Red
        level <= 50f -> Color(0xFFF59E0B) // Amber
        else -> Color(0xFF10B981) // Emerald Green
    }

    val shellColor = MaterialTheme.colorScheme.onSurface

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val strokeWidth = 8.dp.toPx()
        val cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx())

        // 1. Draw the Battery Tip (Terminal)
        val tipWidth = width * 0.4f
        val tipHeight = height * 0.08f
        drawRoundRect(
            color = shellColor,
            topLeft = Offset((width - tipWidth) / 2, 0f),
            size = Size(tipWidth, tipHeight),
            cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
        )

        // 2. Draw the Main Outer Shell
        val shellTop = tipHeight
        val shellHeight = height - tipHeight
        drawRoundRect(
            color = shellColor,
            topLeft = Offset(0f, shellTop),
            size = Size(width, shellHeight),
            cornerRadius = cornerRadius,
            style = Stroke(width = strokeWidth)
        )

        // 3. Draw the Inner Fluid (with padding)
        val fluidPadding = strokeWidth * 1.5f
        val maxFluidHeight = shellHeight - (fluidPadding * 2)
        val currentFluidHeight = maxFluidHeight * (animatedLevel / 100f)

        // Calculate Y position so it fills from the bottom up
        val fluidTop = (shellTop + shellHeight) - fluidPadding - currentFluidHeight

        if (currentFluidHeight > 0) {
            drawRoundRect(
                color = if (isCharging) fluidColor.copy(alpha = glowAlpha) else fluidColor,
                topLeft = Offset(fluidPadding, fluidTop),
                size = Size(width - (fluidPadding * 2), currentFluidHeight),
                cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx())
            )
        }
    }
}