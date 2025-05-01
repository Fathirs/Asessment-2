package com.uts.studentdo.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uts.studentdo.ui.TaskViewModel
import com.uts.studentdo.ui.components.TaskTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    taskViewModel: TaskViewModel,
    onNavigateUp: () -> Unit
) {
    val userPreferences by taskViewModel.userPreferences.collectAsState(
        initial = com.uts.studentdo.data.UserPreferences(
            darkTheme = false,
            sortOrder = "date",
            priorityFilter = 0
        )
    )

    Scaffold(
        topBar = {
            TaskTopAppBar(
                title = "Settings",
                onBackClick = onNavigateUp
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Theme setting
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Theme",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Dark Theme",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )

                        Switch(
                            checked = userPreferences.darkTheme,
                            onCheckedChange = { taskViewModel.updateDarkTheme(it) }
                        )
                    }
                }
            }

            // Default sort order
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Default Sort Order",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SortOrderRadioButton(
                            text = "By Date",
                            selected = userPreferences.sortOrder == "date",
                            onClick = { taskViewModel.updateSortOrder("date") }
                        )

                        SortOrderRadioButton(
                            text = "By Priority",
                            selected = userPreferences.sortOrder == "priority",
                            onClick = { taskViewModel.updateSortOrder("priority") }
                        )

                        SortOrderRadioButton(
                            text = "By Name",
                            selected = userPreferences.sortOrder == "name",
                            onClick = { taskViewModel.updateSortOrder("name") }
                        )
                    }
                }
            }

            // App information
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "About App",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Student Task Manager",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Version 1.0",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "A simple task management app for students built with Kotlin and Jetpack Compose",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun SortOrderRadioButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = onClick)
        )
    }
}