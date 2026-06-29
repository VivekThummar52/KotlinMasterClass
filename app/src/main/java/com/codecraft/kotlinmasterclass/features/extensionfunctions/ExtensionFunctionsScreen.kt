package com.codecraft.kotlinmasterclass.features.extensionfunctions

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
fun ExtensionFunctionsScreen(
    viewModel: ExtensionFunctionsViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            MasterclassTopAppBar(
                title = { Text("Extension Functions") },
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
            // STICKY HEADER: Execution Monitor
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Result Output", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = uiState, style = MaterialTheme.typography.bodyMedium)
                }
            }

            // SCROLLABLE LESSON CONTENT
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ExtensionLessonCard(
                    title = "1. Basic Extension Functions",
                    details = "Syntax: fun ReceiverClass.functionName()",
                    explanation = "Allows you to extend a class with new functionalities without inheritance. Internally, extensions are compiled down to static methods accepting the receiver instance.",
                    actionText = "Format Currency",
                    onAction = { viewModel.runBasicExtensionDemo() }
                )

                ExtensionLessonCard(
                    title = "2. Extension Properties",
                    details = "Syntax: val ReceiverClass.propertyName: Type get() = ...",
                    explanation = "Extensions can mimic properties using explicit custom getters. They do not hold backing fields or actual memory state inside the target object.",
                    actionText = "Count Vowels",
                    onAction = { viewModel.runExtensionPropertyDemo() }
                )

                ExtensionLessonCard(
                    title = "3. Nullable Receivers",
                    details = "Syntax: fun ReceiverClass?.functionName()",
                    explanation = "You can attach extensions to nullable types. This allows users to invoke your utilities without writing safe-calls (?.) or manual null check boundaries.",
                    actionText = "Verify Blank State",
                    onAction = { viewModel.runNullableReceiverDemo() }
                )

                ExtensionLessonCard(
                    title = "4. Static Resolution Gotchas",
                    details = "Pitfall: Type-based vs Runtime-based execution",
                    explanation = "Crucial Note: Extensions are NOT truly polymorphic. They are dispatched statically. Additionally, if an extension conflicts with a member function identically, the member function ALWAYS takes precedence.",
                    actionText = "Test Resolution Rules",
                    onAction = { viewModel.runStaticResolutionDemo() }
                )
            }
        }
    }
}


@Composable
fun ExtensionLessonCard(
    title: String,
    details: String,
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
            Text(text = details, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
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
fun ExtensionFunctionsScreenPreview() {
    KotlinMasterclassTheme {
        ExtensionFunctionsScreen(
            onBackClick = {},
            onSettingsClick = {}
        )
    }
}
