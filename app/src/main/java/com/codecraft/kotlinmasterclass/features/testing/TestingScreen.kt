package com.codecraft.kotlinmasterclass.features.testing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun TestingScreen(
    viewModel: TestingViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val realTimeLog by viewModel.realTimeLog.collectAsState()
    val virtualTimeLog by viewModel.virtualTimeLog.collectAsState()

    Scaffold(
        topBar = {
            MasterclassTopAppBar(
                title = { Text("Coroutines & Turbine") },
                onBackClick = onBackClick,
                onSettingsClick = onSettingsClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Virtual Time Travel", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("Testing asynchronous flows with delays used to take forever. Using 'runTest' and 'Turbine', we manipulate time to execute delays instantly.", style = MaterialTheme.typography.bodyMedium)

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                
                // Real Time Column
                Column(modifier = Modifier.weight(1f)) {
                    Button(onClick = { viewModel.runRealTimeSimulation() }, modifier = Modifier.fillMaxWidth()) {
                        Text("Real Time (6s)")
                    }
                    LogConsole(log = realTimeLog, color = MaterialTheme.colorScheme.errorContainer)
                }

                // Virtual Time Column
                Column(modifier = Modifier.weight(1f)) {
                    Button(onClick = { viewModel.runVirtualTimeSimulation() }, modifier = Modifier.fillMaxWidth()) {
                        Text("Virtual Time")
                    }
                    LogConsole(log = virtualTimeLog, color = MaterialTheme.colorScheme.primaryContainer)
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                Text(
                    text = "Check the 'src/test' folder in this repository to see the actual Turbine unit tests in action!",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun LogConsole(log: List<String>, color: androidx.compose.ui.graphics.Color) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(color, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        log.forEach { entry ->
            Text(entry, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(vertical = 2.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TestingScreenPreview() {
    KotlinMasterclassTheme {
        TestingScreen(
            onBackClick = {},
            onSettingsClick = {}
        )
    }
}

