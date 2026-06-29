package com.codecraft.kotlinmasterclass.features.aicompanion

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Assistant
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.codecraft.kotlinmasterclass.ui.theme.KotlinMasterclassTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AICompanionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )
        setContent {
            KotlinMasterclassTheme {
                val view = LocalView.current
                if (!view.isInEditMode) {
                    SideEffect {
                        val window = (view.context as Activity).window
                        // 1. Set the status bar color to Transparent to let the gradient show
                        window.statusBarColor = android.graphics.Color.TRANSPARENT

                        // 2. Force status bar icons to be light (Dark icons for light background)
                        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                            true
                    }
                }
                AICompanionScreen()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AICompanionScreen(viewModel: AICompanionViewModel = hiltViewModel()) {
    val haptic = LocalHapticFeedback.current
    var selectedTopic by remember { mutableStateOf(viewModel.aITopics.first().topicName) }

    val lightLavender = Color(0xFFF1ECF6)
    val darkLavender = Color(0xFF9470DA)
    val cardBgColor = Color(0xFFFEFEFE)

    val gradientColors = listOf(
        lightLavender,
        Color(0xFFF5E4E6),
        Color(0xFFD7D7F1)
    )

    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(colors = gradientColors))
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = Color.Transparent,
            ) { innerPadding ->
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item { TopBar(haptic) }

                        item {
                            Row {
                                Text(
                                    text = "Good morning, How can I help you?",
                                    fontSize = 24.sp,
                                    lineHeight = 30.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(0.7f)
                                )
                                Spacer(modifier = Modifier.weight(0.3f))
                            }
                        }

                        item { CenterView(darkLavender, cardBgColor) }

                        item { TopicsHeaderView(darkLavender) }

                        item {
                            TopicsCardRow(viewModel, selectedTopic, haptic, darkLavender, { topicName ->
                                selectedTopic = topicName
                            })
                        }

                        item { AIQueriesCards(viewModel) }
                    }

                    Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                        BottomNavigationBar(darkLavender)
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(darkLavender: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp), // Reduced height (~80% of 110dp)
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp), // Reduced height (~80% of 80dp)
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}) {
                    Icon(
                        Icons.Filled.Home,
                        contentDescription = null,
                        tint = darkLavender,
                        modifier = Modifier.size(24.dp)
                    )
                }
                IconButton(onClick = {}) {
                    Icon(
                        Icons.Filled.CalendarMonth,
                        contentDescription = null,
                        tint = Color.Gray.copy(alpha = 0.6f),
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(56.dp)) // Space for the center button

                IconButton(onClick = {}) {
                    Icon(
                        Icons.Outlined.ChatBubbleOutline,
                        contentDescription = null,
                        tint = Color.Gray.copy(alpha = 0.6f),
                        modifier = Modifier.size(24.dp)
                    )
                }
                IconButton(onClick = {}) {
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = null,
                        tint = Color.Gray.copy(alpha = 0.6f),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Floating Center Button
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 8.dp) // Pushed down a bit
                .size(56.dp) // Reduced size (~80% of 70dp)
                .clip(CircleShape)
                .background(darkLavender),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun AIQueriesCards(viewModel: AICompanionViewModel) {
    val queries = viewModel.aiQueries
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        queries.chunked(2).forEach { rowQueries ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowQueries.forEach { query ->
                    QueryCard(
                        modifier = Modifier.weight(1f),
                        query = query
                    )
                }
                if (rowQueries.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun QueryCard(modifier: Modifier = Modifier, query: AIQueries) {
    Card(
        modifier = modifier.height(180.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = query.queryQuestion,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black,
                    maxLines = 2,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = query.queryAnswer,
                    fontSize = 13.sp,
                    color = Color.DarkGray,
                    maxLines = 2,
                    lineHeight = 18.sp
                )
            }

            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray.copy(alpha = 0.3f),
                    contentColor = Color.Black
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Discover",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun TopBar(haptic: HapticFeedback) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically // Centers icons and text
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFF2C2C2C)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Filled.Person,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        ) {
            Text(text = "Hi, Vivek", fontSize = 14.sp, color = Color.DarkGray)
            Text(
                text = "Welcome back",
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
        IconButton(onClick = { haptic.performHapticFeedback(HapticFeedbackType.LongPress) }) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2C2C2C)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.Notifications,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun CenterView(darkLavender: Color, cardBgColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = cardBgColor),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.Assistant,
                        contentDescription = null,
                        tint = darkLavender,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Talk to AI\nassistant",
                    fontSize = 22.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 28.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Let's try it now",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = darkLavender)
                ) {
                    Text(
                        text = "Start Talking",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AICard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                imageVector = Icons.Default.Mic,
                title = "Voice",
                description = "Voice to text Assistant",
                darkLavender = darkLavender,
                cardBgColor = cardBgColor
            )
            AICard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                imageVector = Icons.Default.Image,
                title = "Image",
                description = "Image \nGenerator",
                darkLavender = darkLavender,
                cardBgColor = cardBgColor
            )
        }
    }
}
@Composable
fun TopicsHeaderView(darkLavender: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Topics",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.Black
        )
        Text(
            text = "See all",
            fontSize = 14.sp,
            color = darkLavender,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun TopicsCardRow(
    viewModel: AICompanionViewModel,
    selectedJobRole: String,
    haptic: HapticFeedback,
    darkLavender: Color,
    onTopicClick: (String) -> Unit,
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(viewModel.aITopics) { jobType ->
            val isSelected = selectedJobRole == jobType.topicName
            Surface(
                onClick = {
                    onTopicClick(jobType.topicName)
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                },
                shape = RoundedCornerShape(24.dp),
                color = if (isSelected) darkLavender else Color.White,
                border = if (isSelected) null else BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f))
            ) {
                Text(
                    text = jobType.topicName,
                    color = if (isSelected) Color.White else Color.Black,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun AICard(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    title: String,
    description: String,
    darkLavender: Color,
    cardBgColor: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = cardBgColor),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector,
                        contentDescription = null,
                        tint = darkLavender,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black,
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = description,
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    lineHeight = 18.sp
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AICompanionScreenPreview() {
    KotlinMasterclassTheme {
        AICompanionScreen()
    }
}