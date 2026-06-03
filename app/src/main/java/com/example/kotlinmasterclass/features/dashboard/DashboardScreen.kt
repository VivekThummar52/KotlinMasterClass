package com.example.kotlinmasterclass.features.dashboard

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kotlinmasterclass.features.dashboard.model.TutorialTopic
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Surface
import androidx.compose.ui.draw.clip

// The 6 unique, non-repeating soothing pastel colors
private val PastelBlue = Color(0xFFE3F2FD)
private val PastelGreen = Color(0xFFE8F5E9)
private val PastelYellow = Color(0xFFFFF9C4)
private val PastelPurple = Color(0xFFF3E5F5)
private val PastelCoral = Color(0xFFFFEBEE)
private val PastelOrange = Color(0xFFFFF3E0)
private val PastelCyan = Color(0xFFE0F7FA)
private val PastelPink = Color(0xFFFCE4EC)
private val PastelTeal = Color(0xFFC4ECE8)
private val PastelMint = Color(0xFFE8F5E9)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToCoroutines: () -> Unit,
    onNavigateToScopeFunctions: () -> Unit,
    onNavigateToExtensionFunctions: () -> Unit,
    onNavigateToHigherOrderFunctions: () -> Unit,
    onNavigateToSealedClasses: () -> Unit,
    onNavigateToGenerics: () -> Unit,
    onNavigateToFlow: () -> Unit,
    onNavigateToConcurrency: () -> Unit,
    onNavigateToDelegation: () -> Unit,
    onNavigateToPerformance: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    // Complete catalog list to fully test the grid layout and colors
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
        ),
        TutorialTopic(
            title = "Extension Functions",
            description = "Adding functionality to existing classes without inheritance.",
            containerColor = PastelYellow,
            onClick = onNavigateToExtensionFunctions
        ),
        TutorialTopic(
            title = "Higher-Order Functions",
            description = "Functions that accept other functions as parameters or return them.",
            containerColor = PastelPurple,
            onClick = onNavigateToHigherOrderFunctions
        ),
        TutorialTopic(
            title = "Sealed & Enum Classes",
            description = "Representing restricted class hierarchies and state modeling.",
            containerColor = PastelCoral,
            onClick = onNavigateToSealedClasses
        ),
        TutorialTopic(
            title = "Generics & Variance",
            description = "Understanding out, in, invariant types, and type safety.",
            containerColor = PastelOrange,
            onClick = onNavigateToGenerics
        ),
        TutorialTopic(
            title = "Kotlin Flow",
            description = "Reactive streams: StateFlow, SharedFlow, and operators.",
            containerColor = PastelCyan,
            onClick = onNavigateToFlow
        ),
        TutorialTopic(
            title = "Advanced Concurrency",
            description = "Thread safety, Mutex, and structured exception handling.",
            containerColor = PastelPink,
            onClick = onNavigateToConcurrency // Linked!
        ),
        TutorialTopic(
            title = "Delegation Patterns",
            description = "Class delegation and property interception (lazy, observable).",
            containerColor = PastelTeal,
            onClick = onNavigateToDelegation // Linked!
        ),
        TutorialTopic(
            title = "Performance & Memory",
            description = "Sequences vs Iterables, and zero-allocation Value Classes.",
            containerColor = PastelMint,
            onClick = onNavigateToPerformance
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kotlin Masterclass", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                actions = {
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
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "💡 INSTRUCTIONS: Tap a card below to open its tutorial. Keep your Android Studio Logcat open and filter by 'MasterclassLog' to see background execution results as you interact with the app.",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            var expandedTopicTitle by remember { mutableStateOf<String?>(null) }

            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp), // Tighter spacing for vertical lists
                modifier = Modifier.weight(1f)
            ) {
                items(topics) { topic ->
                    TopicCard(
                        topic = topic,
                        // 2. Check if THIS specific card is the expanded one
                        isExpanded = expandedTopicTitle == topic.title,
                        // 3. Pass a callback to toggle the state
                        onExpandToggle = {
                            expandedTopicTitle = if (expandedTopicTitle == topic.title) {
                                null // Collapse if it's already open
                            } else {
                                topic.title // Expand this one (which auto-collapses others)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TopicCard(
    topic: TutorialTopic,
    isExpanded: Boolean,
    onExpandToggle: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = topic.containerColor,
            contentColor = Color.Black
        ),
        modifier = Modifier
            .fillMaxWidth()
            // 1. HARD CLIP: This destroys the weird white shadow artifacts on the corners
            .clip(RoundedCornerShape(16.dp))
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 80.dp)
                .height(IntrinsicSize.Min),
            // 2. THE ALIGNMENT FIX: This guarantees the text column sits perfectly centered
            // alongside the blue button, rather than floating to the top.
            verticalAlignment = Alignment.CenterVertically
        ) {

            // LEFT ZONE: Expand/Collapse Area
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onExpandToggle
                    )
                    .padding(vertical = 16.dp, horizontal = 20.dp) // Perfect internal spacing
            ) {
                // Header Row (Title + Arrow)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = topic.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    // 3. THE SPACING FIX: This pushes the arrow safely to the far right
                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Expand/Collapse",
                        tint = Color.DarkGray
                    )
                }

                if (isExpanded) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = topic.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray
                    )
                }
            }

            // RIGHT ZONE: Navigation Button
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .width(64.dp)
                    .fillMaxHeight()
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable {
                        Log.d("MasterclassLog", "User navigating to ${topic.title}")
                        topic.onClick()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Open ${topic.title}",
                    tint = Color.White
                )
            }
        }
    }
}