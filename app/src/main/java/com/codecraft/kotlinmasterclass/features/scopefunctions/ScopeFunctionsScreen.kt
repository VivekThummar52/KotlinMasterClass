package com.codecraft.kotlinmasterclass.features.scopefunctions

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
fun ScopeFunctionsScreen(
    viewModel: ScopeFunctionsViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            MasterclassTopAppBar(
                title = { Text("Scope Functions") },
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
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Result Output", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
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
                // let
                ScopeCard(
                    title = "1. let",
                    details = "Context: 'it' | Returns: Lambda result",
                    explanation = "Used frequently for null-checks. Code inside 'let' executes only if the object is not null.",
                    onActionClick = { viewModel.demonstrateLet() }
                )

                // apply
                ScopeCard(
                    title = "2. apply",
                    details = "Context: 'this' | Returns: The context object itself",
                    explanation = "Ideal for configuring an object immediately after instantiation. You can call its properties directly.",
                    onActionClick = { viewModel.demonstrateApply() }
                )

                // also
                ScopeCard(
                    title = "3. also",
                    details = "Context: 'it' | Returns: The context object itself",
                    explanation = "Used for adding side-effects (like logging or debugging) into a call chain without modifying the object.",
                    onActionClick = { viewModel.demonstrateAlso() }
                )

                // run
                ScopeCard(
                    title = "4. run",
                    details = "Context: 'this' | Returns: Lambda result",
                    explanation = "Combines 'let' and 'with'. Great for executing a block on a non-null object that requires property access and returns a computation.",
                    onActionClick = { viewModel.demonstrateRun() }
                )

                // with
                ScopeCard(
                    title = "5. with",
                    details = "Context: 'this' | Returns: Lambda result",
                    explanation = "Not an extension function. Used to group multiple operations on an object when you don't need null-safety checks.",
                    onActionClick = { viewModel.demonstrateWith() }
                )
            }
        }
    }
}


@Composable
fun ScopeCard(
    title: String,
    details: String,
    explanation: String,
    onActionClick: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(text = details, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = explanation, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = onActionClick, modifier = Modifier.fillMaxWidth()) {
                Text("Execute $title")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScopeFunctionsScreenPreview() {
    KotlinMasterclassTheme {
        ScopeFunctionsScreen(
            onBackClick = {},
            onSettingsClick = {}
        )
    }
}
