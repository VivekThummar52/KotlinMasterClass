package com.example.kotlinmasterclass.features.dashboard

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kotlinmasterclass.features.dashboard.model.TutorialTopic

// --- LIGHT PALETTE (Bright Pastels) ---
private val LBlue = Color(0xFFE3F2FD)
private val LGreen = Color(0xFFE8F5E9)
private val LYellow = Color(0xFFFFF9C4)
private val LPurple = Color(0xFFF3E5F5)
private val LCoral = Color(0xFFFFEBEE)
private val LOrange = Color(0xFFFFF3E0)
private val LCyan = Color(0xFFE0F7FA)
private val LPink = Color(0xFFFCE4EC)
private val LTeal = Color(0xFFE0F2F1)
private val LMint = Color(0xFFF1F8E9)
private val LLime = Color(0xFFF9FBE7)
private val LIndigo = Color(0xFFE8EAF6)

// --- DARK PALETTE (Deep, Muted Tones) ---
private val DBlue = Color(0xFF1E3A8A)
private val DGreen = Color(0xFF14532D)
private val DYellow = Color(0xFF713F12)
private val DPurple = Color(0xFF581C87)
private val DCoral = Color(0xFF7F1D1D)
private val DOrange = Color(0xFF7C2D12)
private val DCyan = Color(0xFF164E63)
private val DPink = Color(0xFF831843)
private val DTeal = Color(0xFF134E4A)
private val DMint = Color(0xFF064E3B)
private val DLime = Color(0xFF3F6212)
private val DIndigo = Color(0xFF311B92)

// Helper class for grouping
data class TopicCategory(
    val title: String,
    val topics: List<TutorialTopic>
)

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
    onNavigateToContracts: () -> Unit,
    onNavigateToContextReceivers: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    // Dynamically check the theme to assign the correct palette
    val isDark = isSystemInDarkTheme()

    val categorizedTopics = listOf(
        TopicCategory(
            title = "Core Fundamentals",
            topics = listOf(
                TutorialTopic("Scope Functions", "let, run, with, apply, also explained.", if (isDark) DGreen else LGreen, onNavigateToScopeFunctions),
                TutorialTopic("Extension Functions", "Adding functionality without inheritance.", if (isDark) DYellow else LYellow, onNavigateToExtensionFunctions),
                TutorialTopic("Sealed & Enum Classes", "Restricted hierarchies and state modeling.", if (isDark) DCoral else LCoral, onNavigateToSealedClasses),
                TutorialTopic("Generics & Variance", "out, in, invariant types, and type safety.", if (isDark) DOrange else LOrange, onNavigateToGenerics)
            )
        ),
        TopicCategory(
            title = "Asynchronous & Reactive",
            topics = listOf(
                TutorialTopic("Coroutines", "Scopes, Dispatchers, launch, async.", if (isDark) DBlue else LBlue, onNavigateToCoroutines),
                TutorialTopic("Kotlin Flow", "StateFlow, SharedFlow, and operators.", if (isDark) DCyan else LCyan, onNavigateToFlow),
                TutorialTopic("Advanced Concurrency", "Thread safety, Mutex, exceptions.", if (isDark) DPink else LPink, onNavigateToConcurrency)
            )
        ),
        TopicCategory(
            title = "Advanced Architecture",
            topics = listOf(
                TutorialTopic("Higher-Order Functions", "Functions as parameters or returns.", if (isDark) DPurple else LPurple, onNavigateToHigherOrderFunctions),
                TutorialTopic("Delegation Patterns", "Class and property interception.", if (isDark) DTeal else LTeal, onNavigateToDelegation),
                TutorialTopic("Performance & Memory", "Sequences vs Iterables, Value Classes.", if (isDark) DMint else LMint, onNavigateToPerformance)
            )
        ),
        TopicCategory(
            title = "Expert Compiler Features",
            topics = listOf(
                TutorialTopic(
                    title = "Kotlin Contracts",
                    description = "Custom smart-casting and compile-time guarantees.",
                    containerColor = if (isDark) DLime else LLime,
                    onClick = onNavigateToContracts
                ),
                TutorialTopic(
                    title = "Context Receivers",
                    description = "Requiring multiple scopes without parameter bloat.",
                    containerColor = if (isDark) DIndigo else LIndigo,
                    onClick = onNavigateToContextReceivers // Linked!
                )
            )
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
                    text = "💡 INSTRUCTIONS: Tap a module below to open its sandbox. Keep your Android Studio Logcat open (filter by 'MasterclassLog') to see background execution results.",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            var expandedTopicTitle by remember { mutableStateOf<String?>(null) }

            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                categorizedTopics.forEach { category ->
                    item {
                        Text(
                            text = category.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                        )
                    }

                    items(category.topics) { topic ->
                        TopicCard(
                            topic = topic,
                            isExpanded = expandedTopicTitle == topic.title,
                            onExpandToggle = {
                                expandedTopicTitle = if (expandedTopicTitle == topic.title) null else topic.title
                            }
                        )
                    }
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

    // Dynamically assign text and icon colors to prevent contrast issues in Dark Mode
    val isDark = isSystemInDarkTheme()
    val primaryTextColor = if (isDark) Color(0xFFF8FAFC) else Color(0xFF0F172A)
    val secondaryTextColor = if (isDark) Color(0xFFCBD5E1) else Color(0xFF475569)

    Surface(
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 2.dp,
        color = topic.containerColor, // Background dynamically fed from the parent list
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
                .defaultMinSize(minHeight = 80.dp)
                .height(IntrinsicSize.Min),
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
                    .padding(vertical = 16.dp, horizontal = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = topic.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = primaryTextColor // High contrast theme-aware text
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Expand/Collapse",
                        tint = secondaryTextColor // Theme-aware arrow color
                    )
                }

                if (isExpanded) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = topic.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = secondaryTextColor // Theme-aware description text
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
                    tint = Color.White // The button icon always stays white
                )
            }
        }
    }
}