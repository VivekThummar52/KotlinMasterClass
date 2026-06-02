package com.example.kotlinmasterclass.features.dashboard

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Define our custom soothing pastel palette
private val PastelBlue = Color(0xFFE3F2FD)
private val PastelGreen = Color(0xFFE8F5E9)
private val PastelYellow = Color(0xFFFFF9C4)
private val PastelPurple = Color(0xFFF3E5F5)
private val PastelCoral = Color(0xFFFFEBEE)
private val PastelOrange = Color(0xFFFFF3E0)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToCoroutines: () -> Unit,
    onNavigateToScopeFunctions: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val topics = listOf(
        TutorialTopic(
            title = "Coroutines",
            description = "Scopes, Dispatchers, launch, async, suspend functions.",
            containerColor = PastelBlue,
            onClick = onNavigateToCoroutines
        ),
        TutorialTopic(
            title = "Scope Functions",
            description = "let, run, with, apply, also explained.",
            containerColor = PastelGreen,
            onClick = onNavigateToScopeFunctions
        )
        // Add more items here as we grow the app
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kotlin Masterclass", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                actions = { // Added the Settings Icon
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Visible UI Comment as requested
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text(
                    text = "💡 INSTRUCTIONS: Tap a card below to open its tutorial. Keep your Android Studio Logcat open and filter by 'MasterclassLog' to see background execution results as you interact with the app.",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                // FIX 1: Provide internal padding so the scrollable area leaves room for shadows
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                // FIX 2: Weight ensures the grid pushes down fully, preventing clipping
                modifier = Modifier.weight(1f)
            ) {
                items(topics) { topic ->
                    TopicCard(topic = topic)
                }
            }
        }
    }
}

data class TutorialTopic(
    val title: String,
    val description: String,
    val containerColor: Color,
    val onClick: () -> Unit
)

@Composable
fun TopicCard(topic: TutorialTopic) {
    Card(
        onClick = {
            Log.d("MasterclassLog", "User clicked on ${topic.title} card")
            topic.onClick()
        },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = topic.containerColor,
            contentColor = Color.Black), // Ensuring text is always readable on light pastels
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = topic.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = topic.description,
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray
            )
        }
    }
}