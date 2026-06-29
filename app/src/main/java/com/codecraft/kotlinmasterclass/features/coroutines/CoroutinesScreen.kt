package com.codecraft.kotlinmasterclass.features.coroutines

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codecraft.kotlinmasterclass.ui.components.MasterclassTopAppBar
import androidx.compose.ui.tooling.preview.Preview
import com.codecraft.kotlinmasterclass.ui.theme.KotlinMasterclassTheme
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoroutinesScreen(
    viewModel: CoroutinesViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            MasterclassTopAppBar(
                title = { Text("Coroutines Tutorial") },
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
            // Live Status Board
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 16.dp) // Space between sticky header and scrolling list
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Live Execution Monitor", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = uiState, style = MaterialTheme.typography.bodyMedium)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Takes up the remaining vertical space
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Concept 1: Dispatchers
                ConceptCard(
                    title = "1. Coroutine Dispatchers",
                    explanation = "Dispatchers guide which thread pool handles the execution. Main handles UI elements, IO optimizes for network/disk operations, and Default optimizes for intensive CPU calculations.",
                    instruction = "👉 Tap execution button, look at Logcat, and filter by 'MasterclassLog' to verify thread names changing.",
                    onActionClick = { viewModel.runDispatcherDemo() },
                    actionButtonText = "Run Dispatcher Demo"
                )

                // Concept 2: Launch vs Async
                ConceptCard(
                    title = "2. Launch vs Async & Await",
                    explanation = "Launch is fire-and-forget; it returns a Job object. Async is used when you need a computational result back; it returns a Deferred<T>, and you call .await() to retrieve the data safely.",
                    instruction = "👉 Observe how the sequence executes sequentially using join and await mechanisms in logs.",
                    onActionClick = { viewModel.runLaunchVsAsyncDemo() },
                    actionButtonText = "Run Launch vs Async"
                )

                // Concept 3: Cancellation
                ConceptCard(
                    title = "3. Job Cancellation & Cooperative Design",
                    explanation = "Coroutines can be cancelled before completion. For cancellation to be smooth, internal code must check for active statuses using functions like ensureActive() or yield().",
                    instruction = "👉 Start the long-running job, wait a couple of seconds, then hit 'Cancel Job' to see the immediate handling state.",
                    onActionClick = { viewModel.startCancellableJob() },
                    actionButtonText = "Start Long Job",
                    secondaryAction = {
                        Button(
                            onClick = { viewModel.cancelActiveJob() },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Cancel Active Job")
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun ConceptCard(
    title: String,
    explanation: String,
    instruction: String,
    onActionClick: () -> Unit,
    actionButtonText: String,
    secondaryAction: @Composable (() -> Unit)? = null
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = explanation, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = instruction, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onActionClick) {
                    Text(actionButtonText)
                }
                secondaryAction?.invoke()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CoroutinesScreenPreview() {
    KotlinMasterclassTheme {
        CoroutinesScreen(
            onBackClick = {},
            onSettingsClick = {}
        )
    }
}
