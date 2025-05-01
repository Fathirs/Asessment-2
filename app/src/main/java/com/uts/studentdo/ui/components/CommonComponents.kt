package com.uts.studentdo.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.uts.studentdo.ui.theme.HighPriority
import com.uts.studentdo.ui.theme.LowPriority
import com.uts.studentdo.ui.theme.MediumPriority
import java.text.SimpleDateFormat
import java.util.*

// Date formatter
val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

// Priority-related functions
fun getPriorityColor(priority: Int): Color {
    return when (priority) {
        1 -> LowPriority
        2 -> MediumPriority
        3 -> HighPriority
        else -> Color.Gray
    }
}

fun getPriorityText(priority: Int): String {
    return when (priority) {
        1 -> "Low"
        2 -> "Medium"
        3 -> "High"
        else -> "None"
    }
}

// Standard top app bar with back button
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskTopAppBar(
    title: String,
    showBackButton: Boolean = true,
    onBackClick: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        },
        actions = actions
    )
}

// Confirmation dialog for deleting tasks
@Composable
fun DeleteConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Task") },
        text = { Text("Are you sure you want to delete this task? It will be moved to the recycle bin.") },
        confirmButton = {
            TextButton(onClick = {
                onConfirm()
                onDismiss()
            }) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// Priority selector component
@Composable
fun PrioritySelector(
    selectedPriority: Int,
    onPrioritySelected: (Int) -> Unit
) {
    Column {
        Text("Priority", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PriorityButton(
                text = "Low",
                color = LowPriority,
                selected = selectedPriority == 1,
                onClick = { onPrioritySelected(1) }
            )
            PriorityButton(
                text = "Medium",
                color = MediumPriority,
                selected = selectedPriority == 2,
                onClick = { onPrioritySelected(2) }
            )
            PriorityButton(
                text = "High",
                color = HighPriority,
                selected = selectedPriority == 3,
                onClick = { onPrioritySelected(3) }
            )
        }
    }
}

@Composable
fun PriorityButton(
    text: String,
    color: Color,
    selected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) color else Color.Transparent,
            contentColor = if (selected) Color.White else color
        ),
        border = if (selected) null else ButtonDefaults.outlinedButtonBorder,
    ) {
        Text(text)
    }
}

// Date picker dialog wrapper
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDismiss: () -> Unit,
    onDateSelected: (Long) -> Unit
) {
    val datePickerState = rememberDatePickerState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Due Date") },
        text = {
            DatePicker(
                state = datePickerState,
                modifier = Modifier.padding(16.dp)
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { onDateSelected(it) }
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// Input field with validation
@Composable
fun ValidatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean,
    errorMessage: String = "",
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    maxLines: Int = 1
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            isError = isError,
            modifier = Modifier.fillMaxWidth(),
            singleLine = singleLine,
            maxLines = maxLines
        )

        if (isError) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}