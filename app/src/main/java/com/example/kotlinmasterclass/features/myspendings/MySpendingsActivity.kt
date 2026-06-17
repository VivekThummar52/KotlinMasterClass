package com.example.kotlinmasterclass.features.myspendings

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Circle
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kotlinmasterclass.ui.theme.KotlinMasterclassTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MySpendingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val statusBarColor = 0xFFFAF5F5.toInt()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(statusBarColor, statusBarColor),
            navigationBarStyle = SystemBarStyle.light(statusBarColor, statusBarColor)
        )
        setContent {
            KotlinMasterclassTheme {
                val view = LocalView.current
                if (!view.isInEditMode) {
                    SideEffect {
                        val window = (view.context as Activity).window
                        // 1. Set the status bar color to match your Scaffold (Light)
                        window.statusBarColor = Color(0xFFFAF5F5).toArgb()

                        // 2. Force status bar icons to be light (White)
                        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                            true
                    }
                }
                MySpendingsScreen()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MySpendingsScreen(viewModel: MySpendingsViewModel = hiltViewModel()) {
    val softWhite = Color(0xFFFAF5F5)
    val softLavenderPerfect = Color(0xFFF1EBEE)
    val haptic = LocalHapticFeedback.current
    var progress by remember { mutableFloatStateOf(0.7f) }
    var selectedTab by remember { mutableIntStateOf(0) }

    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = softWhite,
                bottomBar = {
                    MySpendingsBottomNavigation(
                        selectedTab = selectedTab,
                        onTabSelected = { selectedTab = it }
                    )
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 6.dp)
                        .verticalScroll(rememberScrollState()),
                ) {
                    TopBarView(haptic)

                    Spacer(modifier = Modifier.height(24.dp))

                    Column {
                        Text(text = "Good morning,", fontSize = 28.sp, color = Color.Black.copy(alpha = 0.6f))
                        Text(text = "James 👋", fontSize = 28.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    CurrentMonthOverView(softLavenderPerfect, progress)

                    Spacer(modifier = Modifier.height(24.dp))

                    SpendingOverView()

                    Spacer(modifier = Modifier.height(16.dp))

                    SpendingOverViewList(viewModel)

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Recent Transactions",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    RecentTransactionList(viewModel)
                }
            }
        }
    }
}

@Composable
fun RecentTransactionList(viewModel: MySpendingsViewModel) {
    Column(
        modifier = Modifier.padding(horizontal = 4.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        viewModel.recentTransactionsList.forEach { transactionData ->
            TransactionCard(transactionData)
        }
    }
}

@Composable
fun TransactionCard(transactionData: RecentTransactionData) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(transactionData.backgroundColor)),
            modifier = Modifier.size(56.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = transactionData.icon,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = Color(transactionData.iconColor)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 4.dp)
        ) {
            Text(
                text = transactionData.name,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = transactionData.category,
                color = Color.Gray,
                fontSize = 14.sp
            )
        }

        Column(
            modifier = Modifier.padding(vertical = 4.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = transactionData.amount,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = transactionData.date,
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun TopBarView(haptic: HapticFeedback) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { haptic.performHapticFeedback(HapticFeedbackType.LongPress) }) {
            Icon(
                Icons.Default.Apps,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            Icons.Outlined.Notifications,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(28.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Box(contentAlignment = Alignment.TopEnd) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = Color.DarkGray
                )
            }
            Icon(
                Icons.Default.Circle,
                contentDescription = null,
                tint = Color(0xFF6C63FF),
                modifier = Modifier
                    .size(12.dp)
                    .background(Color.White, CircleShape)
                    .padding(1.dp)
            )
        }
    }
}

@Composable
fun CurrentMonthOverView(softLavenderPerfect: Color, progress: Float) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        shape = RoundedCornerShape(28.dp),
        color = softLavenderPerfect,
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Total Balance",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "$7,540.50",
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = Color.Black
                )
                Spacer(Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "This month", fontSize = 14.sp, color = Color.Gray
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "↑ 5.2%",
                        fontSize = 14.sp,
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    modifier = Modifier.size(90.dp),
                    progress = { progress },
                    color = Color(0xFF6C63FF),
                    strokeWidth = 4.dp,
                    trackColor = Color(0xFF6C63FF).copy(alpha = 0.1f),
                    strokeCap = StrokeCap.Round
                )

                Icon(
                    Icons.Default.Waves,
                    contentDescription = null,
                    tint = Color(0xFF6C63FF),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun SpendingOverView() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Spending overview",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.Black
        )
        Text(
            text = "View all",
            fontSize = 14.sp,
            color = Color(0xFF6C63FF),
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun SpendingOverViewList(viewModel: MySpendingsViewModel) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(viewModel.spendingOverviewList) { spendingData ->
            OverviewCard(spendingData)
        }
    }
}

@Composable
fun OverviewCard(spending: SpendingOverviewTypes) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(spending.backgroundColor)),
            modifier = Modifier.size(64.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = spending.icon,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = Color(spending.iconColor)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = spending.category,
            color = Color.Black,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = spending.amount,
            color = Color.Gray,
            fontSize = 14.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KotlinMasterclassTheme {
        MySpendingsScreen()
    }
}

@Composable
fun MySpendingsBottomNavigation(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home, 0),
        BottomNavItem("Analytics", Icons.AutoMirrored.Filled.ShowChart, 1),
        BottomNavItem("Add", Icons.Default.Add, 2),
        BottomNavItem("Cards", Icons.Default.CreditCard, 3),
        BottomNavItem("Profile", Icons.Default.Person, 4)
    )

    val animatedX by animateFloatAsState(
        targetValue = selectedTab.toFloat(),
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow),
        label = "humpX"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .height(72.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
        ) {
            val barHeight = 56.dp.toPx()
            val width = size.width
            val height = size.height
            val itemWidth = width / 5
            val centerX = itemWidth * animatedX + itemWidth / 2
            
            val path = Path().apply {
                val curveWidth = 50.dp.toPx()
                val curveHeight = 16.dp.toPx()
                
                moveTo(0f, height)
                lineTo(0f, height - barHeight)
                
                // Line to the start of the curve
                lineTo(centerX - curveWidth, height - barHeight)
                
                // Smooth curve up
                cubicTo(
                    centerX - curveWidth / 1.5f, height - barHeight,
                    centerX - curveWidth / 2f, height - barHeight - curveHeight,
                    centerX, height - barHeight - curveHeight
                )
                
                // Smooth curve down
                cubicTo(
                    centerX + curveWidth / 2f, height - barHeight - curveHeight,
                    centerX + curveWidth / 1.5f, height - barHeight,
                    centerX + curveWidth, height - barHeight
                )
                
                lineTo(width, height - barHeight)
                lineTo(width, height)
                close()
            }
            
            drawPath(path, color = Color.White)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .height(56.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = selectedTab == index
                val animatedOffset by animateFloatAsState(
                    targetValue = if (isSelected) -12f else 0f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow),
                    label = "iconOffset"
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onTabSelected(index) },
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(bottom = 6.dp)
                            .offset(y = animatedOffset.dp)
                    ) {
                        if (index == 2) {
                            Box(
                                modifier = Modifier
                                    .size(if (isSelected) 52.dp else 44.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF6C63FF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    item.icon,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(if (isSelected) 28.dp else 24.dp)
                                )
                            }
                        } else {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                tint = if (isSelected) Color(0xFF6C63FF) else Color.Gray.copy(alpha = 0.6f),
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = item.name,
                                fontSize = 11.sp,
                                color = if (isSelected) Color(0xFF6C63FF) else Color.Gray.copy(alpha = 0.6f),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

data class BottomNavItem(
    val name: String,
    val icon: ImageVector,
    val index: Int
)
