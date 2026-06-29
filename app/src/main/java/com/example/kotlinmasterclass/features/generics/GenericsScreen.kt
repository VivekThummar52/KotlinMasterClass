package com.example.kotlinmasterclass.features.generics

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
fun GenericsScreen(
    viewModel: GenericsViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            MasterclassTopAppBar(
                title = { Text("Generics & Variance") },
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
            // STICKY HEADER: Evaluation Monitor Dashboard View
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Type Evaluation Monitor", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
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
                GenericsCard(
                    title = "1. Covariance (out)",
                    keyword = "class Producer<out T>",
                    explanation = "Preserves class subtyping constraints. If Apple extends Fruit, then Producer<Apple> can safely be assigned to Producer<Fruit>. This restricts your class to only 'produce' (return) T, making write operations illegal.",
                    actionText = "Test Covariant Assignment",
                    onAction = { viewModel.runCovarianceDemo() }
                )

                GenericsCard(
                    title = "2. Contravariance (in)",
                    keyword = "class Consumer<in T>",
                    explanation = "Reverses class subtyping constraints. Consumer<Fruit> can be assigned to a reference expecting Consumer<Apple>. This restricts your class to only 'consume' (accept as arguments) T, preventing read operations.",
                    actionText = "Test Contravariant Assignment",
                    onAction = { viewModel.runContravarianceDemo() }
                )

                GenericsCard(
                    title = "3. Reified Type Parameters",
                    keyword = "inline fun <reified T>",
                    explanation = "JVM normally applies Type Erasure, destroying generic type information at runtime. By combining inline expansion with the reified keyword, the compiler locks down the specific class type, allowing runtime checks like (item is T).",
                    actionText = "Evaluate Runtime Types",
                    onAction = { viewModel.runReifiedTypeDemo() }
                )
            }
        }
    }
}


@Composable
fun GenericsCard(
    title: String,
    keyword: String,
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
            Text(text = keyword, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
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
fun GenericsScreenPreview() {
    KotlinMasterclassTheme {
        GenericsScreen(
            onBackClick = {},
            onSettingsClick = {}
        )
    }
}
