package com.example.kotlinmasterclass.features.finance

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlinmasterclass.utils.surfaceAnalysisProvider
import com.example.kotlinmasterclass.ui.components.MasterclassTopAppBar
import java.text.NumberFormat
import java.util.Locale

data class FinanceColors(
    val bg: Color,
    val panel: Color,
    val profit: Color,
    val loss: Color,
    val accent: Color,
    val textMain: Color,
    val textMuted: Color
)

val DarkFinanceColors = FinanceColors(
    bg = Color(0xFF0A0E17),
    panel = Color(0xFF131A2A),
    profit = Color(0xFF00E676),
    loss = Color(0xFFFF1744),
    accent = Color(0xFF2962FF),
    textMain = Color.White,
    textMuted = Color(0xFF8E9BAE)
)

val LightFinanceColors = FinanceColors(
    bg = Color(0xFFF5F7FA),
    panel = Color.White,
    profit = Color(0xFF00C853),
    loss = Color(0xFFD50000),
    accent = Color(0xFF2962FF),
    textMain = Color(0xFF1A1A24),
    textMuted = Color(0xFF78909C)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanceCommandCenterScreen(
    viewModel: FinanceViewModel,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    // THE FIX: Listen to the active MaterialTheme token provider directly instead of the system OS state
    val isDarkTheme = !MaterialTheme.colorScheme.surfaceAnalysisProvider()
    val colors = if (isDarkTheme) DarkFinanceColors else LightFinanceColors

    Scaffold(
        topBar = {
            MasterclassTopAppBar(
                title = { Text("Wealth Command", color = colors.textMain, fontWeight = FontWeight.Bold) },
                onBackClick = onBackClick,
                onSettingsClick = onSettingsClick,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.bg,
                    navigationIconContentColor = colors.textMain,
                    actionIconContentColor = colors.textMain
                )
            )
        },
        containerColor = colors.bg
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {

            // --- HERO BALANCE ---
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("TOTAL NET WORTH", color = colors.textMuted, style = MaterialTheme.typography.labelMedium, letterSpacing = 2.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    AnimatedBalanceCounter(state.totalBalance, colors)

                    Spacer(modifier = Modifier.height(12.dp))

                    val isProfit = state.dailyChange >= 0
                    Row(
                        modifier = Modifier.background(
                            if (isProfit) colors.profit.copy(alpha = 0.1f) else colors.loss.copy(alpha = 0.1f),
                            RoundedCornerShape(16.dp)
                        ).padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isProfit) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward,
                            contentDescription = null,
                            tint = if (isProfit) colors.profit else colors.loss,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = formatCurrency(Math.abs(state.dailyChange)) + " Today",
                            color = if (isProfit) colors.profit else colors.loss,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // --- QUICK ACTIONS ---
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ActionButton(Icons.Filled.SwapVert, "Transfer", Modifier.weight(1f), colors.panel, colors.accent)
                    ActionButton(Icons.Filled.AccountBalanceWallet, "Deposit", Modifier.weight(1f), colors.accent, Color.White)
                }
            }

            // --- LIVE ASSET STREAM ---
            item {
                Text("MARKET STREAM", color = colors.textMain, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 24.dp, top = 32.dp, bottom = 16.dp))
            }
            items(state.assets) { asset ->
                AssetCard(asset, colors)
            }

            // --- RECENT TRANSACTIONS ---
            item {
                Text("RECENT ACTIVITY", color = colors.textMain, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 24.dp, top = 32.dp, bottom = 16.dp))
            }
            items(state.transactions) { tx ->
                TransactionRow(tx, colors)
            }
        }
    }
}

@Composable
fun ActionButton(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, modifier: Modifier = Modifier, bgColor: Color, contentColor: Color) {
    Button(
        onClick = { },
        colors = ButtonDefaults.buttonColors(containerColor = bgColor),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.height(64.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, tint = contentColor)
            Text(label, color = contentColor, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AssetCard(asset: Asset, colors: FinanceColors) {
    Card(
        colors = CardDefaults.cardColors(containerColor = colors.panel),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(asset.symbol, color = colors.textMain, fontWeight = FontWeight.Black, fontSize = 18.sp)
                Text(asset.name, color = colors.textMuted, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Text(formatCurrency(asset.currentPrice), color = colors.textMain, fontWeight = FontWeight.Bold)
            }

            val lineColor = if (asset.isPositive) colors.profit else colors.loss

            Box(modifier = Modifier.weight(1f).height(60.dp)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    if (asset.priceHistory.isEmpty()) return@Canvas

                    val max = asset.priceHistory.maxOrNull() ?: 0f
                    val min = asset.priceHistory.minOrNull() ?: 0f
                    val range = (max - min).coerceAtLeast(0.1f)

                    val widthStep = size.width / (asset.priceHistory.size - 1)
                    val path = Path()

                    asset.priceHistory.forEachIndexed { index, price ->
                        val normalizedY = size.height - ((price - min) / range * size.height)
                        val x = index * widthStep

                        if (index == 0) {
                            path.moveTo(x, normalizedY)
                        } else {
                            val prevPrice = asset.priceHistory[index - 1]
                            val prevY = size.height - ((prevPrice - min) / range * size.height)
                            val prevX = (index - 1) * widthStep

                            val controlX = prevX + (x - prevX) / 2
                            path.cubicTo(controlX, prevY, controlX, normalizedY, x, normalizedY)
                        }
                    }

                    drawPath(path = path, color = lineColor, style = Stroke(width = 6f, cap = StrokeCap.Round))
                }
            }
        }
    }
}

@Composable
fun AnimatedBalanceCounter(balance: Float, colors: FinanceColors) {
    val formatted = formatCurrency(balance)

    Row(horizontalArrangement = Arrangement.Center) {
        formatted.forEach { char ->
            AnimatedContent(
                targetState = char,
                transitionSpec = {
                    if (targetState.isDigit() && initialState.isDigit()) {
                        (slideInVertically { height -> height } + fadeIn()) togetherWith
                                (slideOutVertically { height -> -height } + fadeOut())
                    } else {
                        fadeIn() togetherWith fadeOut()
                    }
                },
                label = "balance_ticker"
            ) { charState ->
                Text(
                    text = charState.toString(),
                    color = colors.textMain,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-2).sp
                )
            }
        }
    }
}

@Composable
fun TransactionRow(tx: Transaction, colors: FinanceColors) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val isDeposit = tx.amount > 0
        Box(
            modifier = Modifier.size(48.dp).clip(CircleShape).background(colors.panel),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isDeposit) Icons.Filled.ArrowDownward else Icons.Filled.ArrowUpward,
                contentDescription = null,
                tint = if (isDeposit) colors.profit else colors.textMuted
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(tx.title, color = colors.textMain, fontWeight = FontWeight.Bold)
            Text(tx.timestamp, color = colors.textMuted, style = MaterialTheme.typography.labelSmall)
        }
        Text(
            text = (if (isDeposit) "+" else "") + formatCurrency(tx.amount),
            color = if (isDeposit) colors.profit else colors.textMain,
            fontWeight = FontWeight.Bold
        )
    }
}

fun formatCurrency(amount: Float): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.US)
    return formatter.format(amount)
}