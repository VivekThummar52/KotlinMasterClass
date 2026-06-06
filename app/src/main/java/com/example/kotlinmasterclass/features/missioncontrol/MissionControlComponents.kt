package com.example.kotlinmasterclass.features.missioncontrol

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

internal val MissionBackground = Color(0xFF050914)
internal val MissionPanel = Color(0xFF0B1424)
internal val MissionPanelBright = Color(0xFF101E33)
internal val MissionCyan = Color(0xFF4DE8FF)
internal val MissionBlue = Color(0xFF5B8CFF)
internal val MissionGreen = Color(0xFF62F5A2)
internal val MissionAmber = Color(0xFFFFC857)
internal val MissionRed = Color(0xFFFF5470)
internal val MissionText = Color(0xFFEAF7FF)
internal val MissionMuted = Color(0xFF8EA9BD)

@Composable
internal fun CommandPanel(
    modifier: Modifier = Modifier,
    title: String? = null,
    accent: Color = MissionCyan,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .background(MissionPanel.copy(alpha = 0.94f), RoundedCornerShape(24.dp))
            .border(1.dp, accent.copy(alpha = 0.22f), RoundedCornerShape(24.dp))
            .padding(16.dp)
    ) {
        if (title != null) {
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = accent,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(14.dp))
        }
        content()
    }
}

@Composable
internal fun ReactorCore(
    state: MissionControlState,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "reactor")
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (state.isRunning) 7000 else 18000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "reactor_rotation"
    )
    val pulse by transition.animateFloat(
        initialValue = 0.88f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (state.mode == ReactorMode.CRITICAL) 360 else 1100),
            repeatMode = RepeatMode.Reverse
        ),
        label = "reactor_pulse"
    )

    val accent = state.mode.accentColor()
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val radius = min(size.width, size.height) / 2f
            val center = this.center

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        accent.copy(alpha = if (state.isRunning) 0.3f else 0.08f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = radius
                ),
                radius = radius
            )

            repeat(3) { index ->
                val ringRadius = radius * (0.48f + index * 0.15f)
                drawCircle(
                    color = accent.copy(alpha = 0.16f + index * 0.06f),
                    radius = ringRadius,
                    style = Stroke(width = (1.5f + index).dp.toPx())
                )
            }

            rotate(rotation, center) {
                repeat(6) { index ->
                    val start = index * 60f + if (index % 2 == 0) 8f else 20f
                    drawArc(
                        color = if (index % 2 == 0) accent else MissionBlue,
                        startAngle = start,
                        sweepAngle = 28f,
                        useCenter = false,
                        topLeft = Offset(center.x - radius * 0.67f, center.y - radius * 0.67f),
                        size = Size(radius * 1.34f, radius * 1.34f),
                        style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round),
                        alpha = if (state.isRunning) 0.9f else 0.25f
                    )
                }
            }

            rotate(-rotation * 1.45f, center) {
                repeat(4) { index ->
                    val angle = Math.toRadians((index * 90f).toDouble())
                    val orbit = radius * 0.53f
                    drawCircle(
                        color = accent,
                        radius = 4.dp.toPx(),
                        center = Offset(
                            center.x + cos(angle).toFloat() * orbit,
                            center.y + sin(angle).toFloat() * orbit
                        ),
                        alpha = if (state.isRunning) 1f else 0.2f
                    )
                }
            }

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color.White, accent, accent.copy(alpha = 0.1f)),
                    center = center,
                    radius = radius * 0.34f * pulse
                ),
                radius = radius * 0.31f * pulse
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.9f),
                radius = radius * 0.08f * pulse
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${state.power.toInt()}%",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Black,
                color = MissionText
            )
            Text(
                text = "CORE OUTPUT",
                style = MaterialTheme.typography.labelSmall,
                color = accent,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
internal fun MetricGauge(
    label: String,
    value: Float,
    displayValue: String,
    color: Color,
    modifier: Modifier = Modifier,
    maxValue: Float = 100f
) {
    Column(
        modifier = modifier
            .background(MissionPanelBright, RoundedCornerShape(18.dp))
            .border(1.dp, color.copy(alpha = 0.18f), RoundedCornerShape(18.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label.uppercase(), style = MaterialTheme.typography.labelSmall, color = MissionMuted)
            Box(Modifier.size(7.dp).background(color, CircleShape))
        }
        Spacer(Modifier.height(10.dp))
        Text(
            displayValue,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MissionText
        )
        Spacer(Modifier.height(10.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(Color.White.copy(alpha = 0.08f), CircleShape)
        ) {
            Box(
                Modifier
                    .fillMaxWidth((value / maxValue).coerceIn(0f, 1f))
                    .height(4.dp)
                    .background(color, CircleShape)
            )
        }
    }
}

@Composable
internal fun TelemetryGraph(
    points: List<TelemetryPoint>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val gridColor = MissionCyan.copy(alpha = 0.08f)
        repeat(5) { index ->
            val y = size.height * index / 4f
            drawLine(gridColor, Offset(0f, y), Offset(size.width, y), 1.dp.toPx())
        }
        repeat(7) { index ->
            val x = size.width * index / 6f
            drawLine(gridColor, Offset(x, 0f), Offset(x, size.height), 1.dp.toPx())
        }

        if (points.size < 2) {
            drawLine(
                MissionMuted.copy(alpha = 0.35f),
                Offset(0f, size.height * 0.7f),
                Offset(size.width, size.height * 0.7f),
                2.dp.toPx()
            )
            return@Canvas
        }

        fun buildPath(value: (TelemetryPoint) -> Float, max: Float): Path {
            val path = Path()
            points.forEachIndexed { index, point ->
                val x = index * size.width / (points.size - 1).coerceAtLeast(1)
                val y = size.height - (value(point) / max).coerceIn(0f, 1f) * size.height
                if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            return path
        }

        drawPath(
            buildPath({ it.power }, 100f),
            color = MissionCyan,
            style = Stroke(2.5.dp.toPx(), cap = StrokeCap.Round)
        )
        drawPath(
            buildPath({ it.temperature }, 140f),
            color = MissionRed,
            style = Stroke(2.dp.toPx(), cap = StrokeCap.Round)
        )
        drawPath(
            buildPath({ it.stability }, 100f),
            color = MissionGreen,
            style = Stroke(2.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

@Composable
internal fun RadarDisplay(
    contacts: List<RadarContact>,
    active: Boolean,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "radar")
    val sweep by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (active) 3200 else 10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radar_sweep"
    )

    Canvas(modifier = modifier.aspectRatio(1f)) {
        val radius = min(size.width, size.height) / 2f
        val center = this.center

        drawCircle(
            brush = Brush.radialGradient(
                listOf(MissionCyan.copy(alpha = 0.08f), Color.Transparent),
                center = center,
                radius = radius
            ),
            radius = radius
        )
        repeat(4) { index ->
            drawCircle(
                MissionCyan.copy(alpha = 0.16f),
                radius * (index + 1) / 4f,
                style = Stroke(1.dp.toPx())
            )
        }
        drawLine(MissionCyan.copy(alpha = 0.14f), Offset(center.x, 0f), Offset(center.x, size.height))
        drawLine(MissionCyan.copy(alpha = 0.14f), Offset(0f, center.y), Offset(size.width, center.y))

        rotate(sweep, center) {
            drawArc(
                brush = Brush.sweepGradient(
                    listOf(Color.Transparent, MissionCyan.copy(alpha = 0.42f), Color.Transparent),
                    center = center
                ),
                startAngle = -36f,
                sweepAngle = 36f,
                useCenter = true,
                topLeft = Offset.Zero,
                size = size
            )
            drawLine(
                MissionCyan,
                center,
                Offset(center.x + radius, center.y),
                2.dp.toPx()
            )
        }

        contacts.forEach { contact ->
            val radians = contact.angleDegrees / 180f * PI.toFloat()
            val contactRadius = radius * contact.distance
            val position = Offset(
                center.x + cos(radians) * contactRadius,
                center.y + sin(radians) * contactRadius
            )
            val color = if (contact.threat) MissionRed else MissionGreen
            drawCircle(color.copy(alpha = 0.2f), 9.dp.toPx(), position)
            drawCircle(color, 3.5.dp.toPx(), position)
        }

        drawCircle(MissionCyan, 4.dp.toPx(), center)
    }
}

internal fun ReactorMode.accentColor(): Color = when (this) {
    ReactorMode.OFFLINE -> MissionMuted
    ReactorMode.STARTING -> MissionBlue
    ReactorMode.NOMINAL -> MissionCyan
    ReactorMode.WARNING -> MissionAmber
    ReactorMode.CRITICAL -> MissionRed
    ReactorMode.STABILIZED -> MissionGreen
}
