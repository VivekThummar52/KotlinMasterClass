package com.codecraft.kotlinmasterclass.features.motion

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codecraft.kotlinmasterclass.utils.CurrencyFormatter
import com.codecraft.kotlinmasterclass.ui.components.MasterclassTopAppBar
import androidx.compose.ui.tooling.preview.Preview
import com.codecraft.kotlinmasterclass.ui.theme.KotlinMasterclassTheme
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MotionScreen(
    viewModel: MotionViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Scaffold(
        topBar = {
            MasterclassTopAppBar(
                title = { Text("Physics & Motion UI") },
                onBackClick = onBackClick,
                onSettingsClick = onSettingsClick
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "↓ Pull down on the card to reveal receipt ↓",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.TopCenter).padding(top = 32.dp)
            )

            // The Morphing Component
            MorphingReceiptCard(viewModel)
        }
    }
}

@Composable
fun MorphingReceiptCard(viewModel: MotionViewModel) {
    val coroutineScope = rememberCoroutineScope()
    
    // 0f means completely collapsed. 1f means completely expanded.
    val expandProgress = remember { Animatable(0f) }
    
    // Calculate heights
    val minHeight = 100.dp
    val maxHeight = 450.dp
    val dragThreshold = 200f // Pixels of drag needed to snap open

    // INTERPOLATIONS: These values calculate automatically based on the expandProgress (0.0 to 1.0)
    val currentHeight = minHeight + ((maxHeight - minHeight) * expandProgress.value)
    val cornerRadius = 24.dp - (8.dp * expandProgress.value) // Corners get sharper as it opens
    val contentAlpha = expandProgress.value // 0f (invisible) to 1f (fully visible)
    
    // Subtle scale effect on the total price
    val totalScale = 1f + (0.2f * expandProgress.value)

    Card(
        shape = RoundedCornerShape(cornerRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceBright),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(currentHeight)
            // THE PHYSICS ENGINE: Listen to raw finger drags
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragEnd = {
                        // When the user lets go, decide whether to snap open or closed
                        coroutineScope.launch {
                            val target = if (expandProgress.value > 0.3f) 1f else 0f
                            expandProgress.animateTo(
                                targetValue = target,
                                animationSpec = spring(
                                    dampingRatio = 0.6f, // Medium bouncy
                                    stiffness = Spring.StiffnessLow
                                )
                            )
                        }
                    }
                ) { change, dragAmount ->
                    change.consume()
                    coroutineScope.launch {
                        // Manually update the progress based on pixel drag amount
                        val newProgress = expandProgress.value + (dragAmount / dragThreshold)
                        expandProgress.snapTo(newProgress.coerceIn(0f, 1f))
                    }
                }
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp)
        ) {
            // --- HEADER (Always Visible) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.ShoppingCart, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Success", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(viewModel.date, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                }
                Text(
                    text = viewModel.displayTotal,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.scale(totalScale) // Scales up slightly as you drag
                )
            }

            // --- EXPANDED CONTENT (Fades in based on drag progress) ---
            if (expandProgress.value > 0.01f) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp)
                        .alpha(contentAlpha) // Fades in smoothly
                ) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text("Transaction Details", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(viewModel.transactionId, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    viewModel.items.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(item.name, style = MaterialTheme.typography.bodyMedium)
                            Text(
                                text = CurrencyFormatter.format(item.price),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MotionScreenPreview() {
    KotlinMasterclassTheme {
        MotionScreen(
            onBackClick = {},
            onSettingsClick = {}
        )
    }
}

