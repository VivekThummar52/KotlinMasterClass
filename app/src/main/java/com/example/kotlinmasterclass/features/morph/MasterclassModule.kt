package com.example.kotlinmasterclass.features.morph

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 1. Mock Data Class
data class MasterclassModule(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val containerColor: Color,
    val contentColor: Color
)

val mockModules = listOf(
    MasterclassModule("ui", "UI Engineering", "Canvas & Motion", Icons.Filled.Brush, Color(0xFF311B92), Color.White),
    MasterclassModule("arch", "Architecture", "Clean MVI Flows", Icons.Filled.Code, Color(0xFF004D40), Color.White),
    MasterclassModule("perf", "Performance", "Baseline Profiles", Icons.Filled.Memory, Color(0xFFB71C1C), Color.White)
)

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MorphScreen(
    onBackClick: () -> Unit
) {
    // State to track which module is currently selected (null means show grid)
    var selectedModule by remember { mutableStateOf<MasterclassModule?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Shared Element Morphing") },
                navigationIcon = {
                    IconButton(onClick = { 
                        // If detail is open, go back to grid. Otherwise, leave screen.
                        if (selectedModule != null) selectedModule = null else onBackClick() 
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        
        // 2. The Magic Wrapper: SharedTransitionLayout
        SharedTransitionLayout(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            // 3. AnimatedContent swaps between the Grid and the Detail view
            AnimatedContent(
                targetState = selectedModule,
                label = "morph_transition",
                transitionSpec = {
                    // Smooth crossfade while the shared elements fly across the screen
                    fadeIn(tween(500)) togetherWith fadeOut(tween(500))
                }
            ) { targetModule ->
                
                if (targetModule == null) {
                    // --- STATE A: THE GRID ---
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(mockModules) { module ->
                            Box(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    // 4. SHARED ELEMENT HOOK (The Container)
                                    .sharedElement(
                                        state = rememberSharedContentState(key = "container-${module.id}"),
                                        animatedVisibilityScope = this@AnimatedContent,
                                        boundsTransform = { _, _ -> spring(dampingRatio = 0.8f, stiffness = 300f) }
                                    )
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(module.containerColor)
                                    .clickable { selectedModule = module }
                                    .padding(16.dp)
                            ) {
                                Column(modifier = Modifier.fillMaxSize()) {
                                    Icon(
                                        imageVector = module.icon,
                                        contentDescription = null,
                                        tint = module.contentColor,
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    
                                    // 5. SHARED ELEMENT HOOK (The Text)
                                    Text(
                                        text = module.title,
                                        color = module.contentColor,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.sharedBounds(
                                            sharedContentState = rememberSharedContentState(key = "title-${module.id}"),
                                            animatedVisibilityScope = this@AnimatedContent
                                        )
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // --- STATE B: THE DETAIL VIEW ---
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // The Hero Header (This is what the grid item morphs into)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                // MATCHING HOOK (The Container)
                                .sharedElement(
                                    state = rememberSharedContentState(key = "container-${targetModule.id}"),
                                    animatedVisibilityScope = this@AnimatedContent,
                                    boundsTransform = { _, _ -> spring(dampingRatio = 0.8f, stiffness = 300f) }
                                )
                                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                                .background(targetModule.containerColor)
                                .padding(32.dp),
                            contentAlignment = Alignment.BottomStart
                        ) {
                            Column {
                                Icon(
                                    imageVector = targetModule.icon,
                                    contentDescription = null,
                                    tint = targetModule.contentColor,
                                    modifier = Modifier.size(64.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                // MATCHING HOOK (The Text)
                                Text(
                                    text = targetModule.title,
                                    color = targetModule.contentColor,
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.sharedBounds(
                                        sharedContentState = rememberSharedContentState(key = "title-${targetModule.id}"),
                                        animatedVisibilityScope = this@AnimatedContent
                                    )
                                )
                                Text(
                                    text = targetModule.subtitle,
                                    color = targetModule.contentColor.copy(alpha = 0.7f),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }

                        // The rest of the screen content fades in normally below the header
                        Column(modifier = Modifier.padding(24.dp)) {
                            Text("Course Overview", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "This masterclass module dives deep into advanced Jetpack Compose techniques. Tap the back arrow in the top app bar to watch the header seamlessly morph back into its grid position.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 24.sp
                            )
                        }
                    }
                }
            }
        }
    }
}