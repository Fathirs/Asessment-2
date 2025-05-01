package com.uts.studentdo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.uts.studentdo.data.Task
import com.uts.studentdo.ui.TaskViewModel
import com.uts.studentdo.ui.components.*
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(
    taskId: Long,
    taskViewModel: TaskViewModel,
    onNavigateUp: () -> Unit
) {
    // Load task data
    var task by remember { mutableStateOf<Task?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Form state
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var priority by remember { mutableStateOf(2) }
    var isCompleted by remember { mutableStateOf(false) }

    var titleError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Fetch task data
    LaunchedEffect(taskId) {
        taskViewModel.viewModelScope.launch {
            val fetchedTask = taskViewModel.getTaskById(taskId)
            task = fetchedTask

            fetchedTask?.let {
                title = it.title
                description = it.description
                dueDate = it.dueDate
                priority = it.priority
                isCompleted = it.isCompleted
            }

            isLoading = false
        }
    }

    // Validation function
    fun validateInputs(): Boolean {
        titleError = title.isBlank()
        descriptionError = description.isBlank()
        return !titleError && !descriptionError
    }

    Scaffold(
        topBar = {
            TaskTopAppBar(
                title = "Edit Task",
                onBackClick = onNavigateUp,
                actions = {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete task")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            if (task == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text("Task not found")
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Completion status
                    Row(
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isCompleted,
                            onCheckedChange = { isCompleted = it }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Mark as completed")
                    }

                    // Title input
                    ValidatedTextField(
                        value = title,
                        onValueChange = { title = it; titleError = false },
                        label = "Title",
                        isError = titleError,
                        errorMessage = "Title cannot be empty"
                    )

                    // Description input
                    ValidatedTextField(
                        value = description,
                        onValueChange = { description = it; descriptionError = false },
                        label = "Description",
                        isError = descriptionError,
                        errorMessage = "Description cannot be empty",
                        singleLine = false,
                        maxLines = 5
                    )

                    // Due date selector
                    Column {
                        Text("Due Date", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { showDatePicker = true }) {
                            Text(dateFormatter.format(Date(dueDate)))
                        }
                    }

                    // Priority selector
                    PrioritySelector(
                        selectedPriority = priority,
                        onPrioritySelected = { priority = it }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Update button
                    Button(
                        onClick = {
                            if (validateInputs()) {
                                task?.let {
                                    taskViewModel.updateTask(
                                        it.copy(
                                            title = title,
                                            description = description,
                                            dueDate = dueDate,
                                            priority = priority,
                                            isCompleted = isCompleted
                                        )
                                    )
                                    onNavigateUp()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Update Task")
                    }
                }
            }
        }

        // Date picker dialog
        if (showDatePicker) {
            DatePickerDialog(
                onDismiss = { showDatePicker = false },
                onDateSelected = { dueDate = it }
            )
        }

        // Delete confirmation dialog
        if (showDeleteDialog) {
            DeleteConfirmationDialog(
                onDismiss = { showDeleteDialog = false },
                onConfirm = {
                    task?.let { taskViewModel.deleteTask(it) }
                    onNavigateUp()
                }
            )
        }
    }
}