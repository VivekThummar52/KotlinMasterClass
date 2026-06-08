package com.example.kotlinmasterclass.features.delegation

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DelegationScreen(
    viewModel: DelegationViewModel,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            MasterclassTopAppBar(
                title = { Text("Delegation Patterns") },
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
                    Text("Delegation Output", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
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
                DelegationCard(
                    title = "1. Class Delegation",
                    explanation = "Allows a class to implement an interface by passing the responsibility to an existing object using the 'by' keyword. This achieves the Decorator pattern natively, favoring composition over inheritance.",
                    actionText = "Trigger Delegated Logger",
                    onAction = { viewModel.runClassDelegationDemo() }
                )

                DelegationCard(
                    title = "2. Lazy Delegation",
                    explanation = "The variable is not initialized until it is accessed for the very first time. Subsequent accesses return the cached value. Excellent for heavy database or network clients.",
                    actionText = "Access Lazy Resource",
                    onAction = { viewModel.runLazyDelegateDemo() }
                )

                DelegationCard(
                    title = "3. Observable & Vetoable",
                    explanation = "Observable fires a callback AFTER a property changes. Vetoable fires a callback BEFORE a property changes, allowing you to reject the change by returning false.",
                    actionText = "Test Observable",
                    onAction = { viewModel.runObservableDelegateDemo() },
                    secondaryAction = {
                        OutlinedButton(
                            onClick = { viewModel.runVetoableDelegateDemo() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Test Vetoable (Random Age)")
                        }
                    }
                )

                DelegationCard(
                    title = "4. Custom Property Delegate",
                    explanation = "You can write your own getter/setter interception logic by implementing getValue and setValue operators. Here, we automatically trim and capitalize strings on assignment.",
                    actionText = "Assign Dirty String",
                    onAction = { viewModel.runCustomDelegateDemo() }
                )
            }
        }
    }
}

@Composable
fun DelegationCard(
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
            Spacer(modifier = Modifier.height(8.dp))
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
