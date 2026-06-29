package com.codecraft.kotlinmasterclass.features.calculator

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codecraft.kotlinmasterclass.utils.surfaceAnalysisProvider
import com.codecraft.kotlinmasterclass.ui.components.MasterclassTopAppBar
import androidx.compose.ui.tooling.preview.Preview
import com.codecraft.kotlinmasterclass.ui.theme.KotlinMasterclassTheme
import androidx.hilt.navigation.compose.hiltViewModel

// --- CALCULATOR PALETTES ---
val DarkCalcColors = CalcColors(bg = Color(0xFF000000), panel = Color(0xFF1C1C1E), accent = Color(0xFFFF9F0A), textMain = Color.White, padGray = Color(0xFF333333))
val LightCalcColors = CalcColors(bg = Color(0xFFF2F2F7), panel = Color(0xFFFFFFFF), accent = Color(0xFFFF9500), textMain = Color.Black, padGray = Color(0xFFE5E5EA))

val padButtons = listOf(
    "sin", "cos", "tan", "÷",
    "7", "8", "9", "×",
    "4", "5", "6", "-",
    "1", "2", "3", "+",
    "x", "0", "⌫", "="
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GraphingCalculatorScreen(
    viewModel: GraphingViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val isDarkTheme = !MaterialTheme.colorScheme.surfaceAnalysisProvider()
    val colors = if (isDarkTheme) DarkCalcColors else LightCalcColors

    Scaffold(
        topBar = {
            MasterclassTopAppBar(
                title = { Text("Fluid Graphing", color = colors.textMain) },
                onBackClick = onBackClick, onSettingsClick = onSettingsClick,
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.bg, navigationIconContentColor = colors.textMain, actionIconContentColor = colors.textMain)
            )
        },
        containerColor = colors.bg
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            
            // 1. The Dynamic Display Area
            // Uses AnimatedContent so the text display smoothly transitions into the Canvas Graph
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(if (state.isGraphing) 1f else 0.4f) // Expands to fill screen when graphing
                    .padding(24.dp),
                contentAlignment = if (state.isGraphing) Alignment.Center else Alignment.BottomEnd
            ) {
                AnimatedContent(targetState = state.isGraphing, label = "display_morph") { isGraphing ->
                    if (isGraphing) {
                        // --- THE MATHEMATICAL CANVAS ENGINE ---
                        Card(
                            modifier = Modifier.fillMaxSize(),
                            colors = CardDefaults.cardColors(containerColor = colors.panel),
                            shape = RoundedCornerShape(32.dp)
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val w = size.width
                                val h = size.height
                                val centerX = w / 2f
                                val centerY = h / 2f
                                
                                // Draw Axes
                                drawLine(colors.padGray, Offset(0f, centerY), Offset(w, centerY), 2f)
                                drawLine(colors.padGray, Offset(centerX, 0f), Offset(centerX, h), 2f)

                                // Generate Math Path
                                val path = Path()
                                val scale = 40f // Pixels per math unit
                                var isFirst = true

                                // Sweep across the X axis pixel by pixel
                                for (px in 0..w.toInt() step 2) {
                                    val mathX = (px - centerX) / scale
                                    
                                    // Simulated Parser: If expression contains "sin", graph sine wave.
                                    // Upgraded Simulated Parser
                                    val mathY = when {
                                        state.expression.contains("sin") -> kotlin.math.sin(mathX)
                                        state.expression.contains("cos") -> kotlin.math.cos(mathX)
                                        state.expression.contains("tan") -> kotlin.math.tan(mathX)
                                        // If they type "x×x" (x squared)
                                        state.expression.contains("x×x") -> mathX * mathX * 0.2f
                                        // If they just type "x" (linear diagonal line)
                                        state.expression.contains("x") -> mathX
                                        // If they just type a number, draw a flat horizontal line at that height
                                        state.expression.isNotEmpty() && state.expression.all { it.isDigit() || it == '.' } -> {
                                            state.expression.toFloatOrNull() ?: 0f
                                        }
                                        // Default to a flat line at zero (the X-axis) if empty or invalid
                                        else -> 0f
                                    }

                                    val py = centerY - (mathY * scale)
                                    if (isFirst) {
                                        path.moveTo(px.toFloat(), py)
                                        isFirst = false
                                    } else {
                                        path.lineTo(px.toFloat(), py)
                                    }
                                }
                                drawPath(path, colors.accent, style = Stroke(width = 8f))
                            }
                            
                            // Floating Close Button inside the Graph
                            Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.TopEnd) {
                                Button(onClick = { viewModel.toggleGraphView() }, colors = ButtonDefaults.buttonColors(containerColor = colors.padGray, contentColor = colors.textMain)) {
                                    Text("Close Graph")
                                }
                            }
                        }
                    } else {
                        // Standard Calculator Text Display
                        Text(
                            text = state.expression.ifEmpty { "0" },
                            fontSize = 64.sp,
                            fontWeight = FontWeight.Light,
                            color = colors.textMain,
                            maxLines = 1
                        )
                    }
                }
            }

            // 2. The Morphing Keypad
            // When graphing is true, the entire keypad shrinks and disappears gracefully
            AnimatedVisibility(
                visible = !state.isGraphing,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(padButtons) { btn ->
                        val isAccent = btn == "="
                        val isOperator = btn in listOf("÷", "×", "-", "+", "sin", "cos", "tan")
                        val btnColor = if (isAccent) colors.accent else if (isOperator) colors.panel else colors.padGray
                        val textColor = if (isAccent) Color.White else if (isOperator) colors.accent else colors.textMain

                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clip(CircleShape)
                                .background(btnColor)
                                .clickable {
                                    when (btn) {
                                        "⌫" -> viewModel.backspace()
                                        "=" -> viewModel.toggleGraphView()
                                        else -> viewModel.appendInput(btn)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(btn, color = textColor, fontSize = 28.sp, fontWeight = if (isOperator) FontWeight.Medium else FontWeight.Normal)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GraphingCalculatorScreenPreview() {
    KotlinMasterclassTheme {
        GraphingCalculatorScreen(
            onBackClick = {},
            onSettingsClick = {}
        )
    }
}
