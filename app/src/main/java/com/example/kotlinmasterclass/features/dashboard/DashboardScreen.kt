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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kotlinmasterclass.features.settings.SettingsViewModel
import com.example.kotlinmasterclass.features.settings.ThemePreference
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.composed
import com.example.kotlinmasterclass.ui.components.MasterclassTopAppBar
import kotlinx.coroutines.delay

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
private val LRed = Color(0xFFFFEBEE)
private val LMagenta = Color(0xFFF3E5F5)
private val LCyanWallet = Color(0xFFE0F7FA)


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
private val DRed = Color(0xFF7F1D1D)
private val DMagenta = Color(0xFF4A148C)
private val DCyanWallet = Color(0xFF006064)

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
    onNavigateToCanvas: () -> Unit,
    onNavigateToMotion: () -> Unit,
    onNavigateToMusicPlayer: () -> Unit,
    onNavigateToTesting: () -> Unit,
    onNavigateToGlassWallet: () -> Unit,
    onNavigateToMorph: () -> Unit,
    onNavigateToMissionControl: () -> Unit,
    onNavigateToAiObservatory: () -> Unit,
    onNavigateToSmartCity: () -> Unit,
    onNavigateToWeather: () -> Unit,
    onNavigateToAudioStudio: () -> Unit,
    onNavigateToFinance: () -> Unit,
    onNavigateToSmartHome: () -> Unit,
    onNavigateToGraphingCalculator: () -> Unit,
    onLaunchJobDiscovery: () -> Unit,
    onLaunchMySpendings: () -> Unit,
    onNavigateToSettings: () -> Unit,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    // 1. Observe the app's internal theme setting
    val currentTheme by settingsViewModel.themePreference.collectAsState()
    val isSystemDark = isSystemInDarkTheme()

    // 2. Calculate the actual dark mode state based on app settings
    val isDark = when (currentTheme) {
        ThemePreference.LIGHT -> false
        ThemePreference.DARK -> true
        else -> isSystemDark
    }

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
                    onClick = onNavigateToContextReceivers
                )
            )
        ),
        TopicCategory(
            title = "UI Engineering & Graphics",
            topics = listOf(
                TutorialTopic(
                    title = "Custom Canvas",
                    description = "Drawing hardware metrics and infinite animations.",
                    containerColor = if (isDark) DRed else LRed,
                    onClick = onNavigateToCanvas
                ),
                TutorialTopic(
                    title = "Gesture Physics",
                    description = "Building tactile, spring-driven morphing UI components.",
                    containerColor = if (isDark) DOrange else LOrange,
                    onClick = onNavigateToMotion
                ),
                TutorialTopic(
                    title = "Music Player UI",
                    description = "Infinite rotation, scrubbers, and continuous state.",
                    containerColor = if (isDark) DMagenta else LMagenta,
                    onClick = onNavigateToMusicPlayer
                ),
                TutorialTopic(
                    title = "Glass & 3D Tilt",
                    description = "Frosted glassmorphism with spatial physics mapping.",
                    containerColor = if (isDark) DCyan else LCyan,
                    onClick = onNavigateToGlassWallet
                ),
                TutorialTopic(
                    title = "Shared Element Morph",
                    description = "Spatial transitions across completely different UI states.",
                    containerColor = if (isDark) DYellow else LYellow,
                    onClick = onNavigateToMorph
                ),
                TutorialTopic(
                    title = "Neumorphic Smart Home",
                    description = "Soft UI extrusion shaders and trigonometric radial dial gestures.",
                    containerColor = if (isDark) DTeal else LTeal,
                    onClick = onNavigateToSmartHome
                ),
                TutorialTopic(
                    title = "Fluid Graphing Calculator",
                    description = "Morphing layout geometry and real-time Canvas calculus plotting.",
                    containerColor = if (isDark) DIndigo else LIndigo,
                    onClick = onNavigateToGraphingCalculator
                )
            )
        ),
        TopicCategory(
            title = "Architecture & Testing",
            topics = listOf(
                TutorialTopic(
                    title = "Flows & Turbine",
                    description = "Time-travel testing for asynchronous state streams.",
                    containerColor = if (isDark) DTeal else LTeal,
                    onClick = onNavigateToTesting
                )
            )
        ),
        TopicCategory(
            title = "Flagship Experience",
            topics = listOf(
                TutorialTopic(
                    title = "Mission Control",
                    description = "Operate a live orbital reactor with telemetry, radar, thermal balancing, emergency states, and adaptive command panels.",
                    containerColor = if (isDark) Color(0xFF003B46) else Color(0xFFD9FBFF),
                    onClick = onNavigateToMissionControl
                ),
//                TutorialTopic(
//                    title = "AI System Observatory",
//                    description = "A living visual map of an Agentic AI brain. Watch tokens pulse through concurrent nodes, tools, and fallback recovery routes.",
//                    containerColor = if (isDark) DIndigo else LIndigo,
//                    onClick = onNavigateToAiObservatory
//                ),
                TutorialTopic(
                    title = "Smart City Viewport",
                    description = "A multi-touch camera engine. Pinch, zoom, and pan across a procedurally generated 5000x5000px cyberpunk city with live traffic.",
                    containerColor = if (isDark) DPink else LPink,
                    onClick = onNavigateToSmartCity
                ),
                TutorialTopic(
                    title = "Weather Planetarium",
                    description = "A full 3D rendering engine built purely on Canvas. Watch a point-cloud planet rotate dynamically with live depth-sorting, yaw/pitch gesture math, and weather-driven gradients.",
                    containerColor = if (isDark) DBlue else LBlue,
                    onClick = onNavigateToWeather // Linked!
                ),
                TutorialTopic(
                    title = "Audio Visualizer Studio",
                    description = "A generative FFT spectrum analyzer featuring a 3D vinyl record, reactive beat pulsing, and a mechanical-switch tactile scrub engine.",
                    containerColor = if (isDark) Color(0xFF311B92) else Color(0xFFD1C4E9),
                    onClick = onNavigateToAudioStudio
                ),
                TutorialTopic(
                    title = "Digital Finance Command",
                    description = "A commercial-grade FinTech dashboard. Features a real-time market simulator, Bezier spline Canvas charts, and an odometer-style rolling balance.",
                    containerColor = if (isDark) Color(0xFF0D47A1) else Color(0xFFBBDEFB),
                    onClick = onNavigateToFinance
                )
            )
        ),

        TopicCategory(
            title = "App Modules & Adaptive UI",
            topics = listOf(
                TutorialTopic(
                    title = "Job Discovery Board",
                    description = "A standalone activity demonstrating the adaptive NavigationSuiteScaffold for foldable and tablet scaling.",
                    containerColor = if (isDark) Color(0xFF1B5E20) else Color(0xFFE8F5E9),
                    onClick = onLaunchJobDiscovery
                ),
                TutorialTopic(
                    title = "My Spendings",
                    description = "A standalone activity listing all the spendings for the including Shopping, Entertainment, OTT platforms and a lot more",
                    containerColor = if (isDark) DIndigo else LIndigo,
                    onClick = onLaunchMySpendings
                )
            )
        )
    )

    Scaffold(
        topBar = {
            MasterclassTopAppBar(
                title = { Text("Kotlin Masterclass", fontWeight = FontWeight.Bold) },
                onSettingsClick = onNavigateToSettings,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            // Anchor the entire column's content to the bottom
            verticalArrangement = Arrangement.Bottom
        ) {

            var expandedTopicTitle by remember { mutableStateOf<String?>(null) }

            // --- 1. HOIST THE STATE ---
            val listState = rememberLazyListState()

            // Determine a beautiful pastel color based on the active theme
            val pastelScrollbarColor = if (isDark) Color(0xFF9FA8DA) else Color(0xFFC5CAE9) // Pastel Indigo

            // 1. THE LIST (Now aligned to the bottom with an ergonomic top gap)
            LazyColumn(
                state = listState,
                // The 140.dp top padding acts like Samsung's One UI, pushing the first item
                // down into the thumb's natural resting arc without breaking scrolling.
                contentPadding = PaddingValues(top = 20.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),

                // Forces items to stack from the bottom up if the list is short
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Bottom),

                modifier = Modifier
                    .weight(1f)
                    .pastelScrollbar(
                        state = listState,
                        scrollbarColor = pastelScrollbarColor
                    )
            ) {
                // --- NEW: SCROLL HINT INDICATOR ---
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp), // Space between the hint and the first category
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowUp,
                            contentDescription = "Swipe up to explore",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Swipe up to explore",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                    }
                }

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
                            },
                            isDark = isDark
                        )
                    }
                }
            }

            // 2. THE INSTRUCTIONS CARD (Moved to the bottom)
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    // Replaced vertical padding with just bottom padding to sit flat above nav bar
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                Text(
                    text = "💡 INSTRUCTIONS: Tap a module below to open its sandbox. Keep your Android Studio Logcat open (filter by 'MasterclassLog') to see background execution results.",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun TopicCard(
    topic: TutorialTopic,
    isExpanded: Boolean,
    onExpandToggle: () -> Unit,
    isDark: Boolean
) {
    val interactionSource = remember { MutableInteractionSource() }

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

/**
 * A highly reusable, stateful modifier to draw a smooth, auto-fading scrollbar.
 */
fun Modifier.pastelScrollbar(
    state: LazyListState,
    scrollbarColor: Color,
    thickness: Float = 28f
): Modifier = composed { // THE FIX: 'composed' allows us to remember states inside a modifier

    // 1. The Fading State Engine
    var isFadingOut by remember { mutableStateOf(false) }

    // 2. The Inactivity Timer
    // This restarts automatically every time the user touches or stops touching the list
    LaunchedEffect(state.isScrollInProgress) {
        if (state.isScrollInProgress) {
            // User is actively scrolling, ensure it is fully visible immediately
            isFadingOut = false
        } else {
            // User stopped scrolling. Wait 1.5 seconds, then trigger the fade out
            delay(1500)
            isFadingOut = true
        }
    }

    // 3. The Smooth Fade Animation
    val scrollbarAlpha by animateFloatAsState(
        targetValue = if (isFadingOut) 0f else 0.85f,
        animationSpec = tween(durationMillis = 500), // Takes half a second to fade out
        label = "scrollbar_alpha"
    )

    // 4. The Drawing Logic (Now uses the animated alpha)
    drawWithContent {
        drawContent()

        // If it's completely invisible, skip the heavy math to save performance
        if (scrollbarAlpha == 0f) return@drawWithContent

        val layoutInfo = state.layoutInfo
        val visibleItemsInfo = layoutInfo.visibleItemsInfo

        if (visibleItemsInfo.isEmpty()) return@drawWithContent

        val totalItems = layoutInfo.totalItemsCount
        val visibleItemsCount = visibleItemsInfo.size

        if (visibleItemsCount >= totalItems && state.firstVisibleItemScrollOffset == 0) {
            return@drawWithContent
        }

        val viewportHeight = size.height
        val totalVisibleSize = visibleItemsInfo.sumOf { it.size }
        val averageItemSize = totalVisibleSize.toFloat() / visibleItemsCount

        val estimatedTotalListSize = averageItemSize * totalItems

        val rawThumbHeight = (viewportHeight / estimatedTotalListSize) * viewportHeight
        val thumbHeight = rawThumbHeight.coerceIn(40f, viewportHeight)

        val estimatedScrollY = (state.firstVisibleItemIndex * averageItemSize) + state.firstVisibleItemScrollOffset
        val maxScrollY = (estimatedTotalListSize - viewportHeight).coerceAtLeast(1f)

        val scrollProgress = (estimatedScrollY / maxScrollY).coerceIn(0f, 1f)
        val thumbOffsetY = scrollProgress * (viewportHeight - thumbHeight)

        drawRoundRect(
            color = scrollbarColor,
            topLeft = Offset(x = size.width - thickness - 12f, y = thumbOffsetY),
            size = Size(width = thickness, height = thumbHeight),
            cornerRadius = CornerRadius(x = thickness / 2, y = thickness / 2),
            alpha = scrollbarAlpha // THE FIX: Apply the animated alpha here!
        )
    }
}
