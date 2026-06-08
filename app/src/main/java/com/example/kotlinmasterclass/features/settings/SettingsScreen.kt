package com.example.kotlinmasterclass.features.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kotlinmasterclass.ui.components.MasterclassTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBackClick: () -> Unit
) {
    val currentTheme by viewModel.themePreference.collectAsState()

    Scaffold(
        topBar = {
            MasterclassTopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
                onBackClick = onBackClick,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
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

            // --- APPEARANCE SETTINGS MODULE ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                // Optional but recommended: Add a subtle clip shape if you want perfectly rounded corners
                shape = MaterialTheme.shapes.large
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Appearance",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Theme Selection Options
                    ThemeOptionRow(
                        label = "System Default",
                        selected = currentTheme == ThemePreference.SYSTEM,
                        onClick = { viewModel.updateTheme(ThemePreference.SYSTEM) }
                    )
                    ThemeOptionRow(
                        label = "Light",
                        selected = currentTheme == ThemePreference.LIGHT,
                        onClick = { viewModel.updateTheme(ThemePreference.LIGHT) }
                    )
                    ThemeOptionRow(
                        label = "Dark",
                        selected = currentTheme == ThemePreference.DARK,
                        onClick = { viewModel.updateTheme(ThemePreference.DARK) }
                    )
                }
            }

            // --- FUTURE SETTINGS MODULES WILL GO HERE ---
            // Example Placeholder:
            // Card { Text("Data & Storage") ... }

        }
    }
}

@Composable
fun ThemeOptionRow(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp), // Reduced vertical padding slightly since the Card handles outer spacing
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
    }
}
