package com.codecraft.kotlinmasterclass.features.wallet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Nfc
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codecraft.kotlinmasterclass.ui.components.MasterclassTopAppBar
import kotlinx.coroutines.launch
import androidx.compose.ui.tooling.preview.Preview
import com.codecraft.kotlinmasterclass.ui.theme.KotlinMasterclassTheme
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlassWalletScreen(
    viewModel: WalletViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    // 1. Morphing State
    var isExpanded by remember { mutableStateOf(false) }

    // 2. 3D Tilt States (Pitch and Yaw)
    val tiltX = remember { Animatable(0f) }
    val tiltY = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    val cardHeight by animateDpAsState(
        targetValue = if (isExpanded) 500.dp else 220.dp,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessLow),
        label = "card_height"
    )

    // 3. Animated Background Mesh (Moves infinitely to show off the glass effect)
    val infiniteTransition = rememberInfiniteTransition(label = "bg_mesh")
    val bgOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bg_offset"
    )

    Scaffold(
        topBar = {
            MasterclassTopAppBar(
                title = { Text("Glassmorphism & 3D Tilt", color = Color.White) },
                onBackClick = onBackClick,
                onSettingsClick = onSettingsClick,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        // The vibrant animated background
        containerColor = Color.Black
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF311B92), Color(0xFFC51162), Color(0xFF004D40)),
                        start = Offset(bgOffset, 0f),
                        end = Offset(bgOffset + 500f, 1500f)
                    )
                )
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                // --- THE 3D GLASS CARD ---
                Card(
                    onClick = { isExpanded = !isExpanded },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(cardHeight) // Morphing height
                        .graphicsLayer {
                            // Apply the 3D rotation
                            rotationX = tiltX.value
                            rotationY = tiltY.value
                            cameraDistance = 12f * density // Gives realistic perspective
                        }
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragEnd = {
                                    // The "Premium Heavy Glass" Bounce
                                    coroutineScope.launch {
                                        tiltX.animateTo(
                                            targetValue = 0f,
                                            animationSpec = spring(
                                                // 0.4f gives it a satisfying, subtle wobble before settling
                                                dampingRatio = 0.4f,
                                                // MediumLow stiffness gives it the feeling of physical mass
                                                stiffness = Spring.StiffnessMediumLow
                                            )
                                        )
                                    }
                                    coroutineScope.launch {
                                        tiltY.animateTo(
                                            targetValue = 0f,
                                            animationSpec = spring(
                                                dampingRatio = 0.4f,
                                                stiffness = Spring.StiffnessMediumLow
                                            )
                                        )
                                    }
                                }
                            ) { change, dragAmount ->
                                // ... (Keep your existing dragAmount logic here)
                                change.consume()
                                coroutineScope.launch {
                                    // Map finger drag to axis tilt (inverted for natural feel)
                                    // Coerced to max 25 degrees so it doesn't flip over
                                    val newTiltX = (tiltX.value + dragAmount.y * 0.5f).coerceIn(-25f, 25f)
                                    val newTiltY = (tiltY.value - dragAmount.x * 0.5f).coerceIn(-25f, 25f)
                                    tiltX.snapTo(newTiltX)
                                    tiltY.snapTo(newTiltY)
                                }
                            }
                        },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent) // We paint the glass ourselves
                ) {
                    // --- GLASS STYLING LAYER ---
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            // The Frosted Glass Core: Semi-transparent white/gray
                            .background(Color.White.copy(alpha = 0.15f))
                            // The Specular Highlight: A gradient mimicking light hitting the top left edge
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(Color.White.copy(alpha = 0.3f), Color.Transparent),
                                    center = Offset(0f, 0f),
                                    radius = 800f
                                )
                            )
                            // The Glass Edge: A 1dp semi-transparent border
                            .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(24.dp))
                            .padding(24.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            // Card Header
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("PLATINUM", color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.labelMedium)
                                Icon(Icons.Filled.Nfc, contentDescription = "Contactless", tint = Color.White)
                            }
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            Text(viewModel.balance, color = Color.White, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                            
                            Spacer(modifier = Modifier.height(32.dp))
                            
                            Text(viewModel.cardNumber, color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.titleMedium, letterSpacing = 2.sp)
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(viewModel.cardHolder, color = Color.White.copy(alpha = 0.6f), style = MaterialTheme.typography.labelLarge)

                            // --- EXPANDED DETAILS (Morphs in smoothly) ---
                            AnimatedVisibility(
                                visible = isExpanded,
                                enter = fadeIn() + expandVertically(),
                                exit = fadeOut() + shrinkVertically()
                            ) {
                                Column(modifier = Modifier.padding(top = 32.dp)) {
                                    HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text("Recent Transactions", color = Color.White.copy(alpha = 0.9f), style = MaterialTheme.typography.titleMedium)
                                    Spacer(modifier = Modifier.height(16.dp))
                                    
                                    viewModel.recentTransactions.forEach { tx ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column {
                                                Text(tx.title, color = Color.White)
                                                Text(tx.date, color = Color.White.copy(alpha = 0.5f), style = MaterialTheme.typography.labelSmall)
                                            }
                                            Text(
                                                text = tx.amount, 
                                                color = if (tx.isPositive) Color(0xFF69F0AE) else Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                if (!isExpanded) {
                    Text(
                        text = "Drag the card to tilt in 3D\nTap to expand",
                        color = Color.White.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GlassWalletScreenPreview() {
    KotlinMasterclassTheme {
        GlassWalletScreen(
            onBackClick = {},
            onSettingsClick = {}
        )
    }
}
