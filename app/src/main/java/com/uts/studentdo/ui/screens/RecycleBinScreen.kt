package com.uts.studentdo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.uts.studentdo.data.DeletedTask
import com.uts.studentdo.ui.TaskViewModel
import com.uts.studentdo.ui.components.TaskTopAppBar
import com.uts.studentdo.ui.components.dateFormatter
import com.uts.studentdo.ui.components.getPriorityColor
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecycleBinScreen(
    taskViewModel: TaskViewModel,
    onNavigateUp: () -> Unit
) {
    val deletedTasks by taskViewModel.deletedTasks.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TaskTopAppBar(
                title = "Recycle Bin",
                onBackClick = onNavigateUp
            )
        }
    ) { paddingValues ->
        if (deletedTasks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Recycle bin is empty",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(deletedTasks) { deletedTask ->
                    DeletedTaskItem(
                        deletedTask = deletedTask,
                        onRestore = { taskViewModel.restoreTask(deletedTask) },
                        onPermanentDelete = {
                            taskViewModel.permanentlyDeleteTask(deletedTask)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DeletedTaskItem(
    deletedTask: DeletedTask,
    onRestore: () -> Unit,
    onPermanentDelete: () -> Unit
) {
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = deletedTask.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = deletedTask.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Due date
                Text(
                    text = "Due: ${dateFormatter.format(Date(deletedTask.dueDate))}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.weight(1f))

                // Priority indicator
                val priorityColor = getPriorityColor(deletedTask.priority)
                Badge(
                    containerColor = priorityColor,
                    contentColor = MaterialTheme.colorScheme.surface
                ) {
                    Text(
                        text = when (deletedTask.priority) {
                            1 -> "LOW"
                            2 -> "MED"
                            3 -> "HIGH"
                            else -> ""
                        },
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Deletion date
            Text(
                text = "Deleted on: ${dateFormatter.format(Date(deletedTask.deletedAt))}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                // Restore button
                TextButton(
                    onClick = onRestore,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        Icons.Default.Restore,
                        contentDescription = "Restore"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Restore")
                }

                // Permanent delete button
                TextButton(
                    onClick = { showDeleteConfirmDialog = true },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete permanently"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Delete Permanently")
                }
            }
        }
    }

    // Confirmation dialog for permanent deletion
    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("Delete Permanently") },
            text = { Text("Are you sure you want to permanently delete this task? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onPermanentDelete()
                        showDeleteConfirmDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete Permanently")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}