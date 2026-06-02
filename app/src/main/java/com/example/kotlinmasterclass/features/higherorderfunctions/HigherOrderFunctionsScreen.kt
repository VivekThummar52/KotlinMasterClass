package com.example.kotlinmasterclass.features.higherorderfunctions

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
fun HigherOrderFunctionsScreen(
    viewModel: HigherOrderFunctionsViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Higher-Order Functions & Lambdas") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
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

            // SCROLLABLE LESSON CATALOG
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                HigherOrderCard(
                    title = "1. Function as Parameter",
                    signature = "Syntax: fun call(op: (Int, Int) -> Int)",
                    explanation = "Accepting logic blocks as a parameter pattern removes coupling completely. The lambda is executed internally using the invoke call frame structure.",
                    actionText = "Pass Calculation Lambdas",
                    onAction = { viewModel.runPassFunctionDemo() }
                )

                HigherOrderCard(
                    title = "2. Function as Return Value",
                    signature = "Syntax: fun factory(): (String) -> String",
                    explanation = "Useful for creating configuration strategies dynamically. The method configures environment state rules internally and spits out a ready-to-run, encapsulated task function.",
                    actionText = "Manufacture Formatters",
                    onAction = { viewModel.runReturnFunctionDemo() }
                )

                HigherOrderCard(
                    title = "3. Inline, Noinline & Crossinline",
                    signature = "Optimization Keywords",
                    explanation = "Inlining forces the compiler to inline bytecode to eliminate runtime object instantiation allocation overhead. Use noinline to exclude loops or elements, and crossinline to secure callbacks executing inside nested contexts.",
                    actionText = "Run Inline Optimization Simulation",
                    onAction = { viewModel.runInlineKeywordsDemo() }
                )
            }
        }
    }
}

@Composable
fun HigherOrderCard(
    title: String,
    signature: String,
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
            Text(text = signature, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = explanation, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = onAction, modifier = Modifier.fillMaxWidth()) {
                Text(actionText)
            }
        }
    }
}