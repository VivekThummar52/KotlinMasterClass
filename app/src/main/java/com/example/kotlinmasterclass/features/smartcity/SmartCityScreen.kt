package com.example.kotlinmasterclass.features.smartcity

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kotlinmasterclass.utils.surfaceAnalysisProvider
import com.example.kotlinmasterclass.ui.components.MasterclassTopAppBar
import androidx.compose.ui.tooling.preview.Preview
import com.example.kotlinmasterclass.ui.theme.KotlinMasterclassTheme
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartCityScreen(
    viewModel: SmartCityViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    // THE FIX: Listen to the active MaterialTheme token provider directly instead of the system OS state
    val isDarkTheme = !MaterialTheme.colorScheme.surfaceAnalysisProvider()
    val colors = if (isDarkTheme) DarkCityColors else LightCityColors

    var scale by remember { mutableFloatStateOf(0.3f) }
    var offset by remember { mutableStateOf(Offset(-500f, -500f)) }

    Scaffold(
        topBar = {
            MasterclassTopAppBar(
                title = { Text("Cyberpunk Colony", color = colors.textMain) },
                onBackClick = onBackClick, onSettingsClick = onSettingsClick,
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.bg, navigationIconContentColor = colors.textMain, actionIconContentColor = colors.textMain)
            )
        },
        containerColor = colors.bg
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Canvas(modifier = Modifier.fillMaxSize().clipToBounds().pointerInput(Unit) {
                detectTransformGestures { centroid, pan, zoom, _ ->
                    val newScale = (scale * zoom).coerceIn(0.15f, 1.5f)
                    val centerAdjust = (centroid - offset) * (1f - newScale / scale)
                    scale = newScale
                    offset += pan + centerAdjust
                }
            }) {
                val screenCenterX = (size.width / 2f - offset.x) / scale
                val screenCenterY = (size.height / 2f - offset.y) / scale

                withTransform({ translate(offset.x, offset.y); scale(scale, pivot = Offset.Zero) }) {

                    // Infrastructure Topography Base Grid
                    for (i in 0..4000 step 200) {
                        drawLine(colors.grid, Offset(i.toFloat(), 0f), Offset(i.toFloat(), 4000f), 4f)
                        drawLine(colors.grid, Offset(0f, i.toFloat()), Offset(4000f, i.toFloat()), 4f)
                    }

                    // Live Energy Network Paths
                    val dashEffect = PathEffect.dashPathEffect(floatArrayOf(30f, 30f), phase = state.energyPhase)
                    state.network.forEach { line ->
                        val startNode = state.buildings.find { it.id == line.startId }
                        val endNode = state.buildings.find { it.id == line.endId }
                        if (startNode != null && endNode != null) {
                            val startCenter = Offset(startNode.x + startNode.w / 2, startNode.y + startNode.h / 2)
                            val endCenter = Offset(endNode.x + endNode.w / 2, endNode.y + endNode.h / 2)
                            drawLine(colors.grid, startCenter, endCenter, 12f)
                            drawLine(colors.energy.copy(alpha = 0.8f), startCenter, endCenter, 6f, pathEffect = dashEffect)
                        }
                    }

                    // 3D Vector Building Extrusions
                    state.buildings.forEach { b ->
                        val baseColor = if (b.hasIncident) colors.alert else if (b.isPowerHub) colors.hub else colors.wall
                        val roofC = if (b.hasIncident) colors.alert.copy(alpha = 0.7f) else if (b.isPowerHub) colors.hub.copy(alpha = 0.7f) else colors.roof

                        val layers = 8
                        for (i in 0..layers) {
                            val progress = i.toFloat() / layers
                            val currentHeight = b.heightFactor * progress
                            val shiftX = (b.x - screenCenterX) * currentHeight
                            val shiftY = (b.y - screenCenterY) * currentHeight
                            val layerX = b.x + shiftX
                            val layerY = b.y + shiftY

                            val currentColor = if (i == layers) roofC else baseColor.copy(alpha = 0.6f + (0.4f * progress))
                            drawRect(color = currentColor, topLeft = Offset(layerX, layerY), size = Size(b.w, b.h))

                            if (i == layers) {
                                drawRect(
                                    color = if (b.hasIncident) colors.alert else colors.energy.copy(alpha = 0.5f),
                                    topLeft = Offset(layerX, layerY), size = Size(b.w, b.h), style = Stroke(width = 4f)
                                )
                            }
                        }
                    }
                }
            }

            // HUD Monitor Panel
            Card(
                colors = CardDefaults.cardColors(containerColor = colors.bg.copy(alpha = 0.85f)),
                modifier = Modifier.align(Alignment.BottomCenter).padding(24.dp).fillMaxWidth(0.9f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("POWER GRID LOAD", color = colors.energy, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        Text("${state.powerLoad.toInt()}%", color = colors.textMain, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("ACTIVE INCIDENTS", color = colors.alert, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        Text("${state.incidents}", color = colors.textMain, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SmartCityScreenPreview() {
    KotlinMasterclassTheme {
        SmartCityScreen(
            onBackClick = {},
            onSettingsClick = {}
        )
    }
}
