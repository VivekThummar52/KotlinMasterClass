package com.example.kotlinmasterclass.features.flow

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlowScreen(
    viewModel: FlowViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    
    // Local state to display the last received SharedFlow event
    var lastSharedEvent by remember { mutableStateOf("No events yet.") }

    // Safely collect SharedFlow events
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            lastSharedEvent = event
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kotlin Flow") },
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
            // STICKY HEADER: State & Event Monitor
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("StateFlow (UI State)", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Text(text = uiState, style = MaterialTheme.typography.bodyMedium)
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text("SharedFlow (Latest Event)", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                    Text(text = lastSharedEvent, style = MaterialTheme.typography.bodyMedium)
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
                FlowCard(
                    title = "1. Cold Flows",
                    explanation = "Standard flows are 'cold'. The block of code inside flow { ... } does not execute until .collect() is called. If two different collectors subscribe, the entire code block executes twice from the beginning.",
                    actionText = "Execute Cold Flow",
                    onAction = { viewModel.runColdFlowDemo() }
                )

                FlowCard(
                    title = "2. SharedFlow (Events)",
                    explanation = "SharedFlows are 'hot'. They emit values to all active subscribers simultaneously, regardless of when they started collecting. They are the modern standard for one-time triggers (like showing a Snackbar).",
                    actionText = "Trigger SharedFlow Event",
                    onAction = { viewModel.triggerSharedFlowEvent() }
                )

                FlowCard(
                    title = "3. Flow Operators",
                    explanation = "Flows excel at data transformation. We can chain operators like filter, map, and combine. The suspension happens automatically, pushing transformed data down the pipeline sequentially.",
                    actionText = "Run Transformation Pipeline",
                    onAction = { viewModel.runFlowOperatorsDemo() }
                )
            }
        }
    }
}

@Composable
fun FlowCard(
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