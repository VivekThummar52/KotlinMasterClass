package com.example.kotlinmasterclass.features.sealedclasses

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
fun SealedClassesScreen(
    viewModel: SealedClassesViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sealed & Enum Classes") },
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
            // STICKY HEADER: State Monitor Output Board
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("State Presentation Board", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = uiState, style = MaterialTheme.typography.bodyMedium)
                }
            }

            // SCROLLABLE BODY DETAILS
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ModelingCard(
                    title = "1. Enum Classes (Uniform Constants)",
                    explanation = "Use Enums when you need a simple list of fixed values that share the exact same properties. Each value is a unique static instance, but they cannot carry unique customized constructor state shapes.",
                    actionText = "Evaluate Enum States",
                    onAction = { viewModel.runEnumDemo() }
                )

                ModelingCard(
                    title = "2. Sealed Classes (Stateful Variations)",
                    explanation = "Sealed classes act like supercharged Enums. They restrict class hierarchies but allow individual subclasses to be constructed dynamically with unique payloads (e.g., separating successful structures from error objects).",
                    actionText = "Simulate Success Payload",
                    onAction = { viewModel.runSealedSuccessDemo() },
                    secondaryAction = {
                        OutlinedButton(
                            onClick = { viewModel.runSealedFailureDemo() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Simulate Failure Payload")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ModelingCard(
    title: String,
    explanation: String,
    actionText: String,
    onAction: () -> Unit,
    secondaryAction: @Composable (() -> Unit)? = null
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = explanation, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = onAction, modifier = Modifier.fillMaxWidth()) {
                Text(actionText)
            }
            if (secondaryAction != null) {
                Spacer(modifier = Modifier.height(8.dp))
                secondaryAction()
            }
        }
    }
}