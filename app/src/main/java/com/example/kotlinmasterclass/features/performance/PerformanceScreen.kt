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
import com.example.kotlinmasterclass.ui.components.MasterclassTopAppBar
import androidx.compose.ui.tooling.preview.Preview
import com.example.kotlinmasterclass.ui.theme.KotlinMasterclassTheme
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerformanceScreen(
    viewModel: PerformanceViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            MasterclassTopAppBar(
                title = { Text("Performance & Memory") },
                onBackClick = onBackClick,
                onSettingsClick = onSettingsClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // STICKY HEADER: State Monitor
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Optimization Monitor", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
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
                    title = "1. Sequences (Lazy Evaluation)",
                    explanation = "Sequences process items one by one through the whole pipeline. They stop completely as soon as terminal conditions are met, avoiding unnecessary processing of 500,000+ items.",
                    actionText = "Run Sequence Benchmark",
                    onAction = { viewModel.runSequenceBenchmark() }
                )

                PerformanceCard(
                    title = "2. Iterables (Eager Evaluation)",
                    explanation = "Standard lists create brand-new intermediate collections in memory at every single step (map, filter). For large datasets, this puts massive pressure on the Garbage Collector (GC).",
                    actionText = "Run Iterable Benchmark",
                    onAction = { viewModel.runIterableBenchmark() }
                )

                PerformanceCard(
                    title = "3. Inline Value Classes",
                    explanation = "Value classes provide type safety (e.g. separate Token from String) but the compiler completely eliminates the object allocation, compiling it down to a primitive at runtime.",
                    actionText = "Run Value Class Demo",
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

@Preview(showBackground = true)
@Composable
fun PerformanceScreenPreview() {
    KotlinMasterclassTheme {
        PerformanceScreen(
            onBackClick = {},
            onSettingsClick = {}
        )
    }
}
