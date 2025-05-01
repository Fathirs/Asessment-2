package com.uts.studentdo.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.uts.studentdo.data.Task
import com.uts.studentdo.ui.TaskViewModel
import com.uts.studentdo.ui.components.TaskTopAppBar
import com.uts.studentdo.ui.components.dateFormatter
import com.uts.studentdo.ui.components.getPriorityColor
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    taskViewModel: TaskViewModel,
    navigateToAddTask: () -> Unit,
    navigateToEditTask: (Long) -> Unit,
    navigateToRecycleBin: () -> Unit,
    navigateToSettings: () -> Unit
) {
    val tasks by taskViewModel.tasksWithPreferences.collectAsState(initial = emptyList())
    val userPreferences by taskViewModel.userPreferences.collectAsState(
        initial = com.uts.studentdo.data.UserPreferences(
            darkTheme = false,
            sortOrder = "date",
            priorityFilter = 0
        )
    )

    var showFilterMenu by remember { mutableStateOf(false) }
    var showSortMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TaskTopAppBar(
                title = "Task Manager",
                showBackButton = false,
                actions = {
                    // Filter menu
                    IconButton(onClick = { showFilterMenu = true }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filter tasks")
                    }

                    // Sort menu
                    IconButton(onClick = { showSortMenu = true }) {
                        Icon(Icons.Default.Sort, contentDescription = "Sort tasks")
                    }

                    // Recycle bin button
                    IconButton(onClick = navigateToRecycleBin) {
                        Icon(Icons.Default.Delete, contentDescription = "Recycle bin")
                    }

                    // Settings button
                    IconButton(onClick = navigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = navigateToAddTask) {
                Icon(Icons.Default.Add, contentDescription = "Add task")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Filter dropdown menu
            DropdownMenu(
                expanded = showFilterMenu,
                onDismissRequest = { showFilterMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("All") },
                    onClick = {
                        taskViewModel.updatePriorityFilter(0)
                        showFilterMenu = false
                    },
                    leadingIcon = {
                        if (userPreferences.priorityFilter == 0) {
                            Icon(Icons.Default.Check, contentDescription = null)
                        }
                    }
                )
                DropdownMenuItem(
                    text = { Text("Low Priority") },
                    onClick = {
                        taskViewModel.updatePriorityFilter(1)
                        showFilterMenu = false
                    },
                    leadingIcon = {
                        if (userPreferences.priorityFilter == 1) {
                            Icon(Icons.Default.Check, contentDescription = null)
                        }
                    }
                )
                DropdownMenuItem(
                    text = { Text("Medium Priority") },
                    onClick = {
                        taskViewModel.updatePriorityFilter(2)
                        showFilterMenu = false
                    },
                    leadingIcon = {
                        if (userPreferences.priorityFilter == 2) {
                            Icon(Icons.Default.Check, contentDescription = null)
                        }
                    }
                )
                DropdownMenuItem(
                    text = { Text("High Priority") },
                    onClick = {
                        taskViewModel.updatePriorityFilter(3)
                        showFilterMenu = false
                    },
                    leadingIcon = {
                        if (userPreferences.priorityFilter == 3) {
                            Icon(Icons.Default.Check, contentDescription = null)
                        }
                    }
                )
            }

            // Sort dropdown menu
            DropdownMenu(
                expanded = showSortMenu,
                onDismissRequest = { showSortMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("By Date") },
                    onClick = {
                        taskViewModel.updateSortOrder("date")
                        showSortMenu = false
                    },
                    leadingIcon = {
                        if (userPreferences.sortOrder == "date") {
                            Icon(Icons.Default.Check, contentDescription = null)
                        }
                    }
                )
                DropdownMenuItem(
                    text = { Text("By Priority") },
                    onClick = {
                        taskViewModel.updateSortOrder("priority")
                        showSortMenu = false
                    },
                    leadingIcon = {
                        if (userPreferences.sortOrder == "priority") {
                            Icon(Icons.Default.Check, contentDescription = null)
                        }
                    }
                )
                DropdownMenuItem(
                    text = { Text("By Name") },
                    onClick = {
                        taskViewModel.updateSortOrder("name")
                        showSortMenu = false
                    },
                    leadingIcon = {
                        if (userPreferences.sortOrder == "name") {
                            Icon(Icons.Default.Check, contentDescription = null)
                        }
                    }
                )
            }

            if (tasks.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No tasks found. Add some tasks!",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(tasks) { task ->
                        TaskItem(
                            task = task,
                            onTaskClick = { navigateToEditTask(task.id) },
                            onCheckboxClick = { taskViewModel.toggleTaskCompleted(task) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItem(
    task: Task,
    onTaskClick: () -> Unit,
    onCheckboxClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onTaskClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onCheckboxClick() }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Due: ${dateFormatter.format(Date(task.dueDate))}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Priority indicator
            val priorityColor = getPriorityColor(task.priority)

            Badge(
                containerColor = priorityColor,
                contentColor = MaterialTheme.colorScheme.surface
            ) {
                Text(
                    text = when (task.priority) {
                        1 -> "LOW"
                        2 -> "MED"
                        3 -> "HIGH"
                        else -> ""
                    },
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
    }
}