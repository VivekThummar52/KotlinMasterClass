package com.codecraft.kotlinmasterclass.features.missioncontrol

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.codecraft.kotlinmasterclass.ui.theme.KotlinMasterclassTheme
import androidx.hilt.navigation.compose.hiltViewModel
import com.codecraft.kotlinmasterclass.ui.components.MasterclassTopAppBar
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

// --- DYNAMIC THEME (Place in MissionControlScreen.kt) ---
data class MissionColors(
    val bg: Color,
    val panel: Color,
    val accent: Color,
    val muted: Color,
    val alert: Color,
    val warning: Color,
    val purple: Color,
    val textMain: Color
)

val DarkMissionColors = MissionColors(
    bg = Color(0xFF050B14),
    panel = Color(0xFF0B1421),
    accent = Color(0xFF00E5FF),
    muted = Color(0xFF1D2E45),
    alert = Color(0xFFFF1744),
    warning = Color(0xFFFFEA00),
    purple = Color(0xFFB388FF),
    textMain = Color.White
)

val LightMissionColors = MissionColors(
    bg = Color(0xFFF1F5F9),
    panel = Color(0xFFFFFFFF),
    accent = Color(0xFF0284C7),
    muted = Color(0xFFCBD5E1),
    alert = Color(0xFFE11D48),
    warning = Color(0xFFD97706),
    purple = Color(0xFF7C3AED),
    textMain = Color(0xFF0F172A)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MissionControlScreen(
    viewModel: MissionControlViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val haptics = LocalHapticFeedback.current

    LaunchedEffect(state.mode) {
        if (state.mode == ReactorMode.CRITICAL) {
            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    Scaffold(
        containerColor = MissionBackground,
        topBar = {
            MasterclassTopAppBar(
                title = {
                    Column {
                        Text(
                            "MISSION CONTROL",
                            color = MissionText,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            "ORBITAL REACTOR // AURORA-7",
                            color = MissionMuted,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                },
                onBackClick = onBackClick,
                onSettingsClick = onSettingsClick,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MissionBackground,
                    navigationIconContentColor = MissionText,
                    actionIconContentColor = MissionText
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        listOf(MissionBackground, Color(0xFF071321), MissionBackground)
                    )
                )
        ) {
            BoxWithConstraints(Modifier.fillMaxSize()) {
                val wideLayout = maxWidth >= 760.dp
                MissionControlDashboard(
                    state = state,
                    wideLayout = wideLayout,
                    onStart = viewModel::startReactor,
                    onTargetPowerChange = viewModel::setTargetPower,
                    onCoolantChange = viewModel::setCoolant,
                    onToggleShields = viewModel::toggleShields,
                    onToggleBoost = viewModel::toggleBoost,
                    onStabilize = viewModel::stabilize,
                    onShutdown = viewModel::emergencyShutdown,
                    onToggleAutopilot = viewModel::toggleAutopilot
                )
            }
        }
    }
}

@Composable
private fun MissionControlDashboard(
    state: MissionControlState,
    wideLayout: Boolean,
    onStart: () -> Unit,
    onTargetPowerChange: (Float) -> Unit,
    onCoolantChange: (Float) -> Unit,
    onToggleShields: () -> Unit,
    onToggleBoost: () -> Unit,
    onStabilize: () -> Unit,
    onShutdown: () -> Unit,
    onToggleAutopilot: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            StatusBanner(state)
        }

        item {
            if (wideLayout) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    ReactorPanel(state, Modifier.weight(1.05f))
                    Column(
                        modifier = Modifier.weight(0.95f),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        MetricsGrid(state)
                        androidx.compose.animation.AnimatedVisibility(visible = state.autopilot.isEngaged) {
                            AiNeuralCorePanel(state.autopilot)
                        }
                        ControlPanel(
                            state,
                            onStart,
                            onTargetPowerChange,
                            onCoolantChange,
                            onToggleShields,
                            onToggleBoost,
                            onStabilize,
                            onShutdown,
                            onToggleAutopilot
                        )
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    ReactorPanel(state)
                    MetricsGrid(state)
                    androidx.compose.animation.AnimatedVisibility(visible = state.autopilot.isEngaged) {
                        AiNeuralCorePanel(state.autopilot)
                    }
                    ControlPanel(
                        state,
                        onStart,
                        onTargetPowerChange,
                        onCoolantChange,
                        onToggleShields,
                        onToggleBoost,
                        onStabilize,
                        onShutdown,
                        onToggleAutopilot
                    )
                }
            }
        }

        item {
            if (wideLayout) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    TelemetryPanel(state, Modifier.weight(1.25f))
                    RadarPanel(state, Modifier.weight(0.75f))
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    TelemetryPanel(state)
                    RadarPanel(state)
                }
            }
        }

        item {
            EventLog(state.events)
        }
    }
}

@Composable
private fun StatusBanner(state: MissionControlState) {
    val accent by animateColorAsState(
        targetValue = state.mode.accentColor(),
        animationSpec = tween(400),
        label = "status_color"
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(accent.copy(alpha = 0.1f), RoundedCornerShape(18.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.width(5.dp).height(34.dp).background(accent, CircleShape))
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(
                state.mode.name,
                color = accent,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Black
            )
            AnimatedContent(
                targetState = state.alertMessage,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "status_message"
            ) { message ->
                Text(
                    message,
                    color = MissionText,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Text(
            "T+${state.tick.toString().padStart(4, '0')}",
            color = MissionMuted,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
private fun ReactorPanel(state: MissionControlState, modifier: Modifier = Modifier) {
    CommandPanel(modifier = modifier, title = "Quantum containment", accent = state.mode.accentColor()) {
        ReactorCore(
            state = state,
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
        )
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
            ReactorReadout("FREQUENCY", "${(4.2f + state.power / 52f).formatOne()} THz")

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
                    color = MissionMuted,
                    fontWeight = FontWeight.Bold
                )
            }

            ReactorReadout("CONTAINMENT", "${state.stability.toInt()}%", Alignment.End)
        }
    }
}

@Composable
private fun ReactorReadout(
    label: String,
    value: String,
    alignment: Alignment.Horizontal = Alignment.Start
) {
    Column(horizontalAlignment = alignment) {
        // 1. Value is drawn first (Top)
        Text(value, color = MissionText, fontWeight = FontWeight.Bold)

        // 2. Label is drawn second (Bottom)
        Text(label, color = MissionMuted, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
private fun MetricsGrid(state: MissionControlState) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MetricGauge(
                "Temperature",
                state.temperature,
                "${state.temperature.toInt()} C",
                if (state.temperature > 88f) MissionRed else MissionAmber,
                Modifier.weight(1f),
                maxValue = 140f
            )
            MetricGauge(
                "Stability",
                state.stability,
                "${state.stability.toInt()}%",
                state.mode.accentColor(),
                Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MetricGauge(
                "Shields",
                state.shields,
                "${state.shields.toInt()}%",
                MissionBlue,
                Modifier.weight(1f)
            )
            MetricGauge(
                "Oxygen",
                state.oxygen,
                "${state.oxygen.toInt()}%",
                MissionGreen,
                Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ControlPanel(
    state: MissionControlState,
    onStart: () -> Unit,
    onTargetPowerChange: (Float) -> Unit,
    onCoolantChange: (Float) -> Unit,
    onToggleShields: () -> Unit,
    onToggleBoost: () -> Unit,
    onStabilize: () -> Unit,
    onShutdown: () -> Unit,
    onToggleAutopilot: () -> Unit
) {
    CommandPanel(title = "Command deck") {
        ControlSlider(
            label = "REACTOR OUTPUT",
            value = state.targetPower,
            displayValue = "${state.targetPower.toInt()}%",
            color = MissionCyan,
            enabled = state.isRunning,
            onValueChange = onTargetPowerChange
        )
        Spacer(Modifier.height(10.dp))
        ControlSlider(
            label = "COOLANT FLOW",
            value = state.coolant,
            displayValue = "${state.coolant.toInt()}%",
            color = MissionBlue,
            enabled = state.isRunning,
            onValueChange = onCoolantChange
        )
        Spacer(Modifier.height(12.dp))

        if (!state.isRunning) {
            CommandButton(
                text = "IGNITE REACTOR",
                color = MissionCyan,
                icon = { Icon(Icons.Filled.PowerSettingsNew, null) },
                onClick = onStart,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CommandButton(
                    text = if (state.shieldsOnline) "SHIELDS ON" else "SHIELDS OFF",
                    color = MissionBlue,
                    icon = { Icon(Icons.Filled.Security, null) },
                    onClick = onToggleShields,
                    modifier = Modifier.weight(1f)
                )
                CommandButton(
                    text = if (state.boostActive) "DROP BOOST" else "OVERDRIVE",
                    color = MissionAmber,
                    icon = { Icon(Icons.Filled.Bolt, null) },
                    onClick = onToggleBoost,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(8.dp))
            CommandButton(
                text = if (state.autopilot.isEngaged) "AI AUTOPILOT ACTIVE" else "ENGAGE AI AUTOPILOT",
                color = if (state.autopilot.isEngaged) MissionPurple else MissionMuted,
                onClick = onToggleAutopilot,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CommandButton(
                    text = "STABILIZE",
                    color = MissionGreen,
                    onClick = onStabilize,
                    modifier = Modifier.weight(1f)
                )
                CommandButton(
                    text = "SHUTDOWN",
                    color = MissionRed,
                    icon = { Icon(Icons.Filled.Warning, null) },
                    onClick = onShutdown,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ControlSlider(
    label: String,
    value: Float,
    displayValue: String,
    color: Color,
    enabled: Boolean,
    onValueChange: (Float) -> Unit
) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = MissionMuted, style = MaterialTheme.typography.labelSmall)
        Text(displayValue, color = color, fontWeight = FontWeight.Bold)
    }
    Slider(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        valueRange = 0f..100f,
        colors = SliderDefaults.colors(
            thumbColor = color,
            activeTrackColor = color,
            inactiveTrackColor = Color.White.copy(alpha = 0.12f),
            disabledThumbColor = MissionMuted,
            disabledActiveTrackColor = MissionMuted.copy(alpha = 0.35f)
        )
    )
}

@Composable
private fun CommandButton(
    text: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color.copy(alpha = 0.16f),
            contentColor = color
        ),
        contentPadding = PaddingValues(horizontal = 10.dp)
    ) {
        icon?.invoke()
        if (icon != null) Spacer(Modifier.width(6.dp))
        Text(text, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun TelemetryPanel(state: MissionControlState, modifier: Modifier = Modifier) {
    CommandPanel(modifier, title = "Live telemetry") {
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            LegendDot(MissionCyan, "POWER")
            LegendDot(MissionRed, "HEAT")
            LegendDot(MissionGreen, "STABILITY")
        }
        Spacer(Modifier.height(12.dp))
        TelemetryGraph(
            points = state.telemetry,
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
        )
    }
}

@Composable
private fun LegendDot(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.width(14.dp).height(3.dp).background(color, CircleShape))
        Spacer(Modifier.width(5.dp))
        Text(label, color = MissionMuted, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
private fun RadarPanel(state: MissionControlState, modifier: Modifier = Modifier) {
    CommandPanel(modifier, title = "Proximity radar", accent = MissionGreen) {
        RadarDisplay(
            contacts = state.contacts,
            active = state.isRunning,
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
        )
        Text(
            "${state.contacts.size} CONTACTS // ${state.contacts.count { it.threat }} PRIORITY",
            color = MissionMuted,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun EventLog(events: List<MissionEvent>) {
    CommandPanel(title = "System event stream", accent = MissionBlue) {
        events.forEachIndexed { index, event ->
            val color = when (event.severity) {
                EventSeverity.INFO -> MissionCyan
                EventSeverity.SUCCESS -> MissionGreen
                EventSeverity.WARNING -> MissionAmber
                EventSeverity.DANGER -> MissionRed
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    event.sequence.toString().padStart(4, '0'),
                    color = MissionMuted,
                    style = MaterialTheme.typography.labelSmall
                )
                Spacer(Modifier.width(10.dp))
                Box(Modifier.width(3.dp).height(18.dp).background(color, CircleShape))
                Spacer(Modifier.width(10.dp))
                Text(
                    event.message,
                    color = if (index == 0) MissionText else MissionMuted,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

private fun Float.formatOne(): String = String.format("%.1f", this)

@Preview(showBackground = true)
@Composable
fun MissionControlScreenPreview() {
    KotlinMasterclassTheme {
        MissionControlScreen(
            onBackClick = {},
            onSettingsClick = {}
        )
    }
}
