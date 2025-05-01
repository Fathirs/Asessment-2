package com.uts.studentdo.ui

import androidx.lifecycle.*
import com.uts.studentdo.data.DeletedTask
import com.uts.studentdo.data.Task
import com.uts.studentdo.data.TaskRepository
import com.uts.studentdo.data.UserPreferences
import com.uts.studentdo.data.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.*

class TaskViewModel(
    private val taskRepository: TaskRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    // Get user preferences
    val userPreferences: Flow<UserPreferences> = userPreferencesRepository.userPreferencesFlow

    // Combine tasks with user preferences to apply sorting and filtering
    val tasksWithPreferences = combine(
        taskRepository.allTasks,
        userPreferences
    ) { tasks, preferences ->
        var filteredTasks = tasks

        // Apply priority filter if not set to "All"
        if (preferences.priorityFilter > 0) {
            filteredTasks = filteredTasks.filter { it.priority == preferences.priorityFilter }
        }

        // Apply sorting based on the sort order preference
        filteredTasks = when (preferences.sortOrder) {
            "priority" -> filteredTasks.sortedByDescending { it.priority }
            "name" -> filteredTasks.sortedBy { it.title }
            else -> filteredTasks.sortedBy { it.dueDate } // Default: sort by date
        }

        filteredTasks
    }

    // Get deleted tasks (recycle bin)
    val deletedTasks: Flow<List<DeletedTask>> = taskRepository.deletedTasks

    // Insert a new task
    fun insertTask(title: String, description: String, dueDate: Date, priority: Int) {
        viewModelScope.launch {
            val task = Task(
                title = title,
                description = description,
                dueDate = dueDate.time,
                priority = priority
            )
            taskRepository.insertTask(task)
        }
    }

    // Get task by ID
    suspend fun getTaskById(taskId: Long): Task? {
        return taskRepository.getTaskById(taskId)
    }

    // Update an existing task
    fun updateTask(task: Task) {
        viewModelScope.launch {
            taskRepository.updateTask(task)
        }
    }

    // Delete a task (move to recycle bin)
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.moveTaskToRecycleBin(task)
        }
    }

    // Restore a task from recycle bin
    fun restoreTask(deletedTask: DeletedTask) {
        viewModelScope.launch {
            taskRepository.restoreTaskFromRecycleBin(deletedTask)
        }
    }

    // Permanently delete a task from recycle bin
    fun permanentlyDeleteTask(deletedTask: DeletedTask) {
        viewModelScope.launch {
            taskRepository.permanentlyDeleteTask(deletedTask.id)
        }
    }

    // Toggle task completion status
    fun toggleTaskCompleted(task: Task) {
        viewModelScope.launch {
            taskRepository.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }

    // Update the dark theme preference
    fun updateDarkTheme(darkTheme: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateDarkTheme(darkTheme)
        }
    }

    // Update the sort order preference
    fun updateSortOrder(sortOrder: String) {
        viewModelScope.launch {
            userPreferencesRepository.updateSortOrder(sortOrder)
        }
    }

    // Update the priority filter
    fun updatePriorityFilter(priorityFilter: Int) {
        viewModelScope.launch {
            userPreferencesRepository.updatePriorityFilter(priorityFilter)
        }
    }
}

// Factory to create the ViewModel with dependencies
class TaskViewModelFactory(
    private val taskRepository: TaskRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(taskRepository, userPreferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}