package com.example.kotlinmasterclass.features.observatory

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.kotlinmasterclass.ui.components.MasterclassTopAppBar
import androidx.compose.ui.tooling.preview.Preview
import com.example.kotlinmasterclass.ui.theme.KotlinMasterclassTheme
import androidx.hilt.navigation.compose.hiltViewModel

// --- CYBER THEME COLORS ---
val CyberBg = Color(0xFF0D0E15)
val CyberPanel = Color(0xFF151828)
val NodeIdle = Color(0xFF3B4261)
val NodeActive = Color(0xFF00E5FF) // Cyan
val NodeSuccess = Color(0xFF00E676) // Neon Green
val NodeFailed = Color(0xFFFF1744) // Neon Red

data class UiNode(val id: String, val label: String, val x: Float, val y: Float)

// Normalized coordinates (0.0 = Left/Top, 1.0 = Right/Bottom)
val graphLayout = listOf(
    UiNode("user", "User", 0.5f, 0.05f),
    UiNode("router", "LLM Router", 0.5f, 0.2f),
    UiNode("agent_search", "Search Agent", 0.2f, 0.45f),
    UiNode("agent_logic", "Logic Agent", 0.8f, 0.45f),
    UiNode("tool_web", "Web Engine", 0.2f, 0.7f),
    UiNode("tool_db", "Vector DB", 0.8f, 0.7f),
    UiNode("synthesizer", "Synthesizer", 0.5f, 0.9f)
)

val graphEdges = listOf(
    Pair("user", "router"),
    Pair("router", "agent_search"),
    Pair("router", "agent_logic"),
    Pair("agent_search", "tool_web"),
    Pair("agent_logic", "tool_db"),
    Pair("tool_web", "synthesizer"),
    Pair("tool_db", "synthesizer"),
    Pair("router", "tool_db") // Fallback route
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiObservatoryScreen(
    viewModel: AiObservatoryViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = CyberBg,
        topBar = {
            MasterclassTopAppBar(
                title = { Text("AI System Observatory", color = Color.White) },
                onBackClick = onBackClick,
                onSettingsClick = onSettingsClick,
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CyberBg, navigationIconContentColor = Color.White, actionIconContentColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // HEADER: Prompt Monitor
            Card(
                colors = CardDefaults.cardColors(containerColor = CyberPanel),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("ACTIVE PROMPT", color = NodeActive, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(state.prompt, color = Color.White, style = MaterialTheme.typography.bodyMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
                }
            }

            // MIDDLE: The Canvas Neural Graph
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(CyberPanel, RoundedCornerShape(16.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                NeuralGraphCanvas(state)
            }

            // BOTTOM: Metrics and Controls
            Spacer(modifier = Modifier.height(16.dp))
            MetricsPanel(state.metrics)
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = { viewModel.startSimulation() },
                    colors = ButtonDefaults.buttonColors(containerColor = NodeActive),
                    modifier = Modifier.weight(1f).height(56.dp)
                ) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = null, tint = Color.Black)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("EXECUTE", color = Color.Black, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = { viewModel.toggleFailureInjection() },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = if (state.failureInjected) NodeFailed else Color.White),
                    border = null,
                    modifier = Modifier.weight(1f).height(56.dp).background(CyberPanel, RoundedCornerShape(28.dp))
                ) {
                    Icon(Icons.Filled.BugReport, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (state.failureInjected) "CHAOS: ARMED" else "CHAOS: OFF")
                }
            }
        }
    }
}

@Composable
fun NeuralGraphCanvas(state: ObservatoryState) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // 1. Draw Wires (Edges)
        graphEdges.forEach { edge ->
            val from = graphLayout.find { it.id == edge.first } ?: return@forEach
            val to = graphLayout.find { it.id == edge.second } ?: return@forEach
            
            val start = Offset(from.x * w, from.y * h)
            val end = Offset(to.x * w, to.y * h)
            
            val isFallback = edge.first == "router" && edge.second == "tool_db"
            val wireColor = if (isFallback && state.phase == AiPhase.FAILED_RECOVERY) NodeFailed else NodeIdle.copy(alpha = 0.3f)

            // Draw curved Bezier path for sci-fi look
            drawWire(start, end, wireColor)
        }

        // 2. Draw Moving Data Pulses
        state.activePulses.forEach { pulse ->
            val from = graphLayout.find { it.id == pulse.from } ?: return@forEach
            val to = graphLayout.find { it.id == pulse.to } ?: return@forEach
            
            val start = Offset(from.x * w, from.y * h)
            val end = Offset(to.x * w, to.y * h)
            
            // Calculate exact point on the cubic bezier curve
            val pulsePos = getBezierPoint(start, end, pulse.progress)
            
            // Draw glowing pulse
            drawCircle(
                brush = Brush.radialGradient(listOf(Color.White, NodeActive.copy(alpha = 0.5f), Color.Transparent), center = pulsePos, radius = 30f),
                radius = 30f,
                center = pulsePos
            )
            drawCircle(Color.White, 6f, pulsePos)
        }

        // 3. Draw Nodes
        graphLayout.forEach { node ->
            val nodeState = state.nodes[node.id] ?: NodeState.IDLE
            val center = Offset(node.x * w, node.y * h)
            val color = when (nodeState) {
                NodeState.ACTIVE -> NodeActive
                NodeState.SUCCESS -> NodeSuccess
                NodeState.FAILED -> NodeFailed
                NodeState.IDLE -> NodeIdle
            }

            // Glow Effect
            if (nodeState == NodeState.ACTIVE || nodeState == NodeState.FAILED) {
                drawCircle(
                    brush = Brush.radialGradient(listOf(color.copy(alpha = 0.4f), Color.Transparent), center = center, radius = 80f),
                    radius = 80f,
                    center = center
                )
            }

            // Core Node
            drawCircle(CyberBg, 24f, center) // Inner cutout
            drawCircle(color, 24f, center, style = Stroke(width = 6f)) // Ring
            drawCircle(color, 12f, center) // Center dot
        }
    }
}

// Draws an elegant "S" curve connecting two nodes
fun DrawScope.drawWire(start: Offset, end: Offset, color: Color) {
    val path = Path().apply {
        moveTo(start.x, start.y)
        // Control points create the curve: pulling out vertically before moving horizontally
        val controlY = start.y + (end.y - start.y) * 0.5f
        cubicTo(start.x, controlY, end.x, controlY, end.x, end.y)
    }
    drawPath(path, color, style = Stroke(width = 4f, cap = StrokeCap.Round))
}

// Pure math: Calculates the (x,y) coordinates at a specific percentage (t) along a Cubic Bezier curve.
// This allows the pulse to follow the exact curved wire flawlessly.
fun getBezierPoint(start: Offset, end: Offset, t: Float): Offset {
    val controlY = start.y + (end.y - start.y) * 0.5f
    val c1 = Offset(start.x, controlY)
    val c2 = Offset(end.x, controlY)

    val u = 1f - t
    val tt = t * t
    val uu = u * u
    val uuu = uu * u
    val ttt = tt * t

    val x = (uuu * start.x) + (3 * uu * t * c1.x) + (3 * u * tt * c2.x) + (ttt * end.x)
    val y = (uuu * start.y) + (3 * uu * t * c1.y) + (3 * u * tt * c2.y) + (ttt * end.y)
    return Offset(x, y)
}

@Composable
fun MetricsPanel(metrics: AiMetrics) {
    Row(
        modifier = Modifier.fillMaxWidth().background(CyberPanel, RoundedCornerShape(16.dp)).padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MetricItem("LATENCY", "${metrics.latencyMs}ms", NodeActive)
        MetricItem("IN TOKENS", "${metrics.inputTokens}", Color.LightGray)
        MetricItem("OUT TOKENS", "${metrics.outputTokens}", Color.LightGray)
        MetricItem("AGENTS", "${metrics.activeAgents}", NodeSuccess)
    }
}

@Composable
fun MetricItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = color.copy(alpha = 0.7f), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, color = color, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
    }
}

@Preview(showBackground = true)
@Composable
fun AiObservatoryScreenPreview() {
    KotlinMasterclassTheme {
        AiObservatoryScreen(
            onBackClick = {},
            onSettingsClick = {}
        )
    }
}
