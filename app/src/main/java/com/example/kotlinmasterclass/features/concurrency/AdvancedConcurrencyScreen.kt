package com.example.kotlinmasterclass.features.concurrency

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
fun AdvancedConcurrencyScreen(
    viewModel: AdvancedConcurrencyViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Concurrency & Safety") },
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
            // STICKY HEADER: State Monitor
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Execution Monitor", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
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
                ConcurrencyCard(
                    title = "1. Race Conditions",
                    explanation = "When multiple threads write to the same shared memory location simultaneously, data gets overwritten and lost. This simulation launches 1,000 coroutines to increment a counter by 1. Notice how the final result is almost never 1,000.",
                    actionText = "Trigger Race Condition",
                    onAction = { viewModel.runRaceConditionDemo() }
                )

                ConcurrencyCard(
                    title = "2. Mutex (Mutual Exclusion)",
                    explanation = "A Mutex locks a critical section of code. If a coroutine hits a locked Mutex, it suspends until the lock is freed. This guarantees that only one coroutine can modify the shared state at any exact microsecond, ensuring 100% accuracy.",
                    actionText = "Run with Mutex Lock",
                    onAction = { viewModel.runMutexDemo() }
                )

                ConcurrencyCard(
                    title = "3. SupervisorScope & Handlers",
                    explanation = "By default, if a child coroutine throws an exception, it cancels its parent and all sibling coroutines. Wrapping tasks in a supervisorScope breaks this chain, allowing independent tasks to finish even if one fails.",
                    actionText = "Simulate Component Crash",
                    onAction = { viewModel.runExceptionHandlingDemo() }
                )
            }
        }
    }
}

@Composable
fun ConcurrencyCard(
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