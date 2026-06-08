package com.example.kotlinmasterclass.features.weather

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlinmasterclass.ui.components.MasterclassTopAppBar
import com.example.kotlinmasterclass.utils.surfaceAnalysisProvider
import kotlin.math.*

val DarkWeatherColors = WeatherColors(
    bg = Color(0xFF03040A), star = Color(0xFFE2E8F0), atmosphere = Color(0xFF1E3A8A), textMain = Color.White,
    clear = Color(0xFFFFB300), rain = Color(0xFF00B0FF), storm = Color(0xFFD500F9), cloudy = Color(0xFF90A4AE)
)

val LightWeatherColors = WeatherColors(
    bg = Color(0xFFE0F2FE), star = Color(0xFF0369A1), atmosphere = Color(0xFF7DD3FC), textMain = Color(0xFF0F172A),
    clear = Color(0xFFD97706), rain = Color(0xFF0284C7), storm = Color(0xFF9333EA), cloudy = Color(0xFF475569)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherPlanetariumScreen(
    viewModel: WeatherViewModel,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val selectedLoc by viewModel.selectedLocation.collectAsState()

    // THE FIX: Listen to the active MaterialTheme token provider
    val isDarkTheme = !MaterialTheme.colorScheme.surfaceAnalysisProvider()
    val colors = if (isDarkTheme) DarkWeatherColors else LightWeatherColors

    var yaw by remember { mutableFloatStateOf(0f) }
    var pitch by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis { yaw -= 0.005f }
        }
    }

    val primaryColor by animateColorAsState(
        targetValue = when (selectedLoc.type.name) {
            "CLEAR" -> colors.clear
            "RAIN" -> colors.rain
            "STORM" -> colors.storm
            else -> colors.cloudy
        },
        animationSpec = tween(1000), label = ""
    )

    val spherePoints = remember {
        val numPoints = 600
        val phi = Math.PI * (3 - sqrt(5.0))
        List(numPoints) { i ->
            val y = 1 - (i / (numPoints - 1).toFloat()) * 2
            val r = sqrt(1 - y * y)
            val theta = phi * i

            // Using Constructor reflection or assumes Point3D is imported correctly
            Point3D((cos(theta) * r).toFloat(), y.toFloat(), (sin(theta) * r).toFloat(), i % 10 == 0)
        }
    }

    Scaffold(
        topBar = {
            MasterclassTopAppBar(
                title = { Text("Weather Planetarium", color = colors.textMain) },
                onBackClick = onBackClick, onSettingsClick = onSettingsClick,
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.bg, navigationIconContentColor = colors.textMain, actionIconContentColor = colors.textMain)
            )
        },
        containerColor = colors.bg
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Canvas(modifier = Modifier.fillMaxSize().pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    yaw -= dragAmount.x * 0.01f
                    pitch = (pitch - dragAmount.y * 0.01f).coerceIn(-1.5f, 1.5f)
                }
            }) {
                val center = Offset(size.width / 2f, size.height / 2.5f)
                val radius = size.width * 0.42f

                drawCircle(
                    brush = Brush.radialGradient(listOf(primaryColor.copy(alpha = 0.2f), colors.atmosphere.copy(alpha = 0.05f), Color.Transparent), center = center, radius = radius * 1.4f),
                    center = center, radius = radius * 1.4f
                )
                drawCircle(color = colors.bg.copy(alpha = 0.8f), radius = radius, center = center)

                val cosPitch = cos(pitch); val sinPitch = sin(pitch)
                val cosYaw = cos(yaw); val sinYaw = sin(yaw)

                spherePoints.forEach { point ->
                    val py = point.y * cosPitch - point.z * sinPitch
                    val pzTemp = point.y * sinPitch + point.z * cosPitch
                    val px = point.x * cosYaw - pzTemp * sinYaw
                    val pz = point.x * sinYaw + pzTemp * cosYaw

                    if (pz > -0.2f) {
                        val screenX = center.x + (px * radius)
                        val screenY = center.y + (py * radius)
                        val depthScale = ((pz + 1) / 2).coerceIn(0.1f, 1f)
                        val alpha = depthScale * 0.8f
                        val pointRadius = 2f + (depthScale * 3f)

                        val pointColor = if (point.isNode) primaryColor else colors.star
                        if (point.isNode) drawCircle(pointColor.copy(alpha = alpha * 0.4f), radius = pointRadius * 3, center = Offset(screenX, screenY))
                        drawCircle(pointColor.copy(alpha = alpha), radius = pointRadius, center = Offset(screenX, screenY))
                    }
                }
            }

            Column(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(bottom = 32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("${selectedLoc.temperature}°C", color = colors.textMain, style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.Black, fontSize = 72.sp)
                Text(selectedLoc.name.uppercase(), color = primaryColor, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, letterSpacing = 4.sp)
                Spacer(modifier = Modifier.height(32.dp))

                LazyRow(contentPadding = PaddingValues(horizontal = 24.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(viewModel.locations) { loc ->
                        val isSelected = selectedLoc.id == loc.id
                        val icon = when(loc.type.name) {
                            "CLEAR" -> Icons.Filled.WbSunny
                            "RAIN" -> Icons.Filled.Air
                            "STORM" -> Icons.Filled.Thunderstorm
                            else -> Icons.Filled.Cloud
                        }
                        Card(
                            colors = CardDefaults.cardColors(containerColor = if (isSelected) primaryColor.copy(alpha = 0.2f) else colors.textMain.copy(alpha = 0.05f)),
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier.clickable { viewModel.selectLocation(loc.id) }
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                                Icon(icon, contentDescription = null, tint = if (isSelected) primaryColor else colors.textMain)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(loc.name.split(",")[0], color = colors.textMain, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }
        }
    }
}