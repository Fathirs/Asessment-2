package com.uts.studentdo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uts.studentdo.ui.TaskViewModel
import com.uts.studentdo.ui.components.*
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    taskViewModel: TaskViewModel,
    onNavigateUp: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var priority by remember { mutableStateOf(2) } // Default medium priority

    var titleError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }

    var showDatePicker by remember { mutableStateOf(false) }

    // Validation function
    fun validateInputs(): Boolean {
        titleError = title.isBlank()
        descriptionError = description.isBlank()
        return !titleError && !descriptionError
    }

    Scaffold(
        topBar = {
            TaskTopAppBar(
                title = "Add New Task",
                onBackClick = onNavigateUp
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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

            // Save button
            Button(
                onClick = {
                    if (validateInputs()) {
                        taskViewModel.insertTask(
                            title = title,
                            description = description,
                            dueDate = Date(dueDate),
                            priority = priority
                        )
                        onNavigateUp()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Task")
            }
        }

        // Date picker dialog
        if (showDatePicker) {
            DatePickerDialog(
                onDismiss = { showDatePicker = false },
                onDateSelected = { dueDate = it }
            )
        }
    }
}