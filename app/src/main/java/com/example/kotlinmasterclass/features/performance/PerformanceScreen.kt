package com.example.kotlinmasterclass.features.performance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerformanceScreen(
    viewModel: PerformanceViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Performance & Memory") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // STICKY HEADER: Benchmark Monitor
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Benchmark Output", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = uiState, style = MaterialTheme.typography.bodyMedium)
                }
            }

            // SCROLLABLE LESSON SHEETS
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PerformanceCard(
                    title = "1. Iterables (Eager)",
                    explanation = "Standard collections process the entire list at every step. If you map 500k items, it creates a new 500k item list in memory before moving to the next operator. This is fast for small lists, but dangerous for massive data sets.",
                    actionText = "Run Iterable Benchmark",
                    onAction = { viewModel.runIterableBenchmark() }
                )

                PerformanceCard(
                    title = "2. Sequences (Lazy)",
                    explanation = "Sequences evaluate element-by-element. If you chain operators and only request 10 items via .take(10), it processes items one at a time and stops completely once it hits 10. Massive memory savings and massive speed boosts for large operations.",
                    actionText = "Run Sequence Benchmark",
                    onAction = { viewModel.runSequenceBenchmark() }
                )

                PerformanceCard(
                    title = "3. Value Classes",
                    explanation = "By using '@JvmInline value class', you create a strict data type (like 'Password' instead of 'String') to prevent accidental bugs. At compile time, it acts like a class. At runtime, the JVM strips the class away and uses the raw primitive, saving you from heavy heap allocations.",
                    actionText = "Evaluate Value Class",
                    onAction = { viewModel.runValueClassDemo() }
                )
            }
        }
    }
}

@Composable
fun PerformanceCard(
    title: String,
    explanation: String,
    actionText: String,
    onAction: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = explanation, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = onAction, modifier = Modifier.fillMaxWidth()) {
                Text(actionText)
            }
        }
    }
}