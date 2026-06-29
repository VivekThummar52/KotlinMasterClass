package com.example.kotlinmasterclass.features.musicplayer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Path
 import androidx.compose.ui.graphics.StrokeCap
 import androidx.compose.ui.graphics.drawscope.Stroke
 import androidx.compose.ui.graphics.drawscope.translate
 import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.material.icons.filled.Android
import com.example.kotlinmasterclass.ui.components.MasterclassTopAppBar
import androidx.compose.ui.tooling.preview.Preview
import com.example.kotlinmasterclass.ui.theme.KotlinMasterclassTheme

import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayerScreen(
    viewModel: MusicPlayerViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val isPlaying by viewModel.isPlaying.collectAsState()
    val currentPosition by viewModel.currentPosition.collectAsState()

    // UI State for the Morphing Layout
    var isInfoExpanded by remember { mutableStateOf(false) }

    // --- ANIMATION STATES ---

    // 1. Vinyl Spin (Infinite while playing)
    val rotation = remember { Animatable(0f) }
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            while (true) {
                rotation.animateTo(
                    targetValue = rotation.value + 360f,
                    animationSpec = tween(durationMillis = 6000, easing = LinearEasing)
                )
            }
        } else {
            rotation.stop()
        }
    }

    // 2. Tonearm (Pin) Swing Physics
    // Swings inward when playing, outward when paused
    val armAngle by animateFloatAsState(
        targetValue = if (isPlaying) 12f else 0f, // Reduced swing from 25f down to 12f
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow),
        label = "tonearm_swing"
    )

    // 3. Morphing Sizes for Info View Transition
    val vinylSize by animateDpAsState(
        targetValue = if (isInfoExpanded) 140.dp else 280.dp,
        animationSpec = spring(dampingRatio = 0.8f, stiffness = Spring.StiffnessLow),
        label = "vinyl_size"
    )

    Scaffold(
        topBar = {
            MasterclassTopAppBar(
                title = { Text(if (isInfoExpanded) "Track Details" else "Now Playing", style = MaterialTheme.typography.titleMedium) },
                onBackClick = onBackClick,
                onSettingsClick = onSettingsClick,
                actions = {
                    IconButton(onClick = { isInfoExpanded = !isInfoExpanded }) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "Info",
                            tint = if (isInfoExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // --- VINYL & TONEARM ASSEMBLY ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp), // Fixed height boundary to keep layout stable during morph
                contentAlignment = Alignment.Center
            ) {
                // 1. The Spinning Vinyl Record
                Box(
                    modifier = Modifier
                        .size(vinylSize) // Animates between 280dp and 140dp
                        .clip(CircleShape)
                        .background(Color(0xFF1E1E1E))
                        .rotate(rotation.value),
                    contentAlignment = Alignment.Center
                ) {
                    // Record Grooves
                    Box(modifier = Modifier.size(vinylSize * 0.95f).clip(CircleShape).background(Color(0xFF2C2C2C)))
                    Box(modifier = Modifier.size(vinylSize * 0.85f).clip(CircleShape).background(Color(0xFF1E1E1E)))
                    Box(modifier = Modifier.size(vinylSize * 0.75f).clip(CircleShape).background(Color(0xFF2C2C2C)))

                    // Center Label (Theme colored)
                    Box(
                        modifier = Modifier
                            .size(vinylSize * 0.35f)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        // Authentic Android Logo!
                        Icon(
                            imageVector = Icons.Filled.Android,
                            contentDescription = "Android Mascot",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(vinylSize * 0.25f) // Increased size slightly for impact
                        )
                    }

                    // Spindle Hole
//                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color.Black))
                }

                // 2. The Tonearm (Pin)
                // Only visible when not in expanded info mode to save space
                androidx.compose.animation.AnimatedVisibility(
                    visible = !isInfoExpanded,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Canvas(
                        modifier = Modifier
                            .size(300.dp)
                            .graphicsLayer {
                                transformOrigin = TransformOrigin(0.85f, 0.1f)
                                rotationZ = armAngle
                            }
                    ) {
                        val pivotX = size.width * 0.85f
                        val pivotY = size.height * 0.1f

                        // 1. Draw Pivot Base
                        drawCircle(color = Color.LightGray, radius = 32f, center = Offset(pivotX, pivotY))
                        drawCircle(color = Color(0xFF333333), radius = 24f, center = Offset(pivotX, pivotY))
                        drawCircle(color = Color.Gray, radius = 12f, center = Offset(pivotX, pivotY))
                        drawCircle(color = Color.Black, radius = 4f, center = Offset(pivotX, pivotY))

                        // 2. Draw the Curved Arm (Adjusted to rest on the outer edge)
                        val endX = size.width * 0.72f   // Pushed outward (Right)
                        val endY = size.height * 0.62f  // Lifted slightly (Up)

                        val armPath = Path().apply {
                            moveTo(pivotX, pivotY)
                            cubicTo(
                                x1 = size.width * 0.95f, y1 = size.height * 0.3f, // Bulge further right
                                x2 = size.width * 0.85f, y2 = size.height * 0.5f,
                                x3 = endX, y3 = endY
                            )
                        }

                        drawPath(
                            path = armPath,
                            color = Color(0xFFD7CCC8),
                            style = Stroke(width = 14f, cap = StrokeCap.Round)
                        )

                        // 3. Draw the Headshell (Needle Cartridge)
                        translate(left = endX, top = endY) {
                            // Adjusted to 18 degrees to perfectly match the new tangent of the curve
                            rotate(degrees = 55f, pivot = Offset.Zero) {

                                drawRoundRect(
                                    color = Color(0xFF1E1E1E),
                                    topLeft = Offset(-16f, 0f),
                                    size = Size(32f, 55f),
                                    cornerRadius = CornerRadius(6f, 6f)
                                )

                                drawRoundRect(
                                    color = Color(0xFFEF5350),
                                    topLeft = Offset(-6f, 45f),
                                    size = Size(12f, 16f),
                                    cornerRadius = CornerRadius(2f, 2f)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- MORPHING INFO PANEL ---
            AnimatedVisibility(
                visible = isInfoExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Person, contentDescription = "Artist", modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(viewModel.trackArtist, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text("Album: ${viewModel.albumName}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("Released: ${viewModel.releaseDate}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Filled.ThumbUp, contentDescription = "Likes", tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(viewModel.likes, style = MaterialTheme.typography.labelMedium)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Filled.ThumbDown, contentDescription = "Dislikes", tint = MaterialTheme.colorScheme.error)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(viewModel.dislikes, style = MaterialTheme.typography.labelMedium)
                            }
                        }
                    }
                }
            }

            // --- BASIC TRACK INFO ---
            if (!isInfoExpanded) {
                Text(
                    text = viewModel.trackTitle,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = viewModel.trackArtist,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // --- THE SCRUBBER & MEDIA CONTROLS ---
            Column(modifier = Modifier.fillMaxWidth()) {
                Slider(
                    value = currentPosition,
                    onValueChange = { viewModel.seekTo(it) },
                    valueRange = 0f..viewModel.trackDuration,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = viewModel.formatTimestamp(currentPosition),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = viewModel.formatTimestamp(viewModel.trackDuration),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Play/Pause Control
            FloatingActionButton(
                onClick = { viewModel.togglePlayPause() },
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 48.dp).size(72.dp)
            ) {
                if (isPlaying) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(modifier = Modifier.width(6.dp).height(24.dp).background(MaterialTheme.colorScheme.onPrimary, RoundedCornerShape(2.dp)))
                        Box(modifier = Modifier.width(6.dp).height(24.dp).background(MaterialTheme.colorScheme.onPrimary, RoundedCornerShape(2.dp)))
                    }
                } else {
                    Icon(Icons.Filled.PlayArrow, contentDescription = "Play", modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MusicPlayerScreenPreview() {
    KotlinMasterclassTheme {
        MusicPlayerScreen(
            onBackClick = {},
            onSettingsClick = {}
        )
    }
}
