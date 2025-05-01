package com.uts.studentdo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uts.studentdo.ui.TaskViewModel
import com.uts.studentdo.ui.TaskViewModelFactory
import com.uts.studentdo.ui.theme.StudentTaskManagerTheme
import com.uts.studentdo.ui.screens.AppNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val taskRepository = (application as TaskManagerApplication).taskRepository
        val userPreferencesRepository = (application as TaskManagerApplication).userPreferencesRepository

        setContent {
            // Create view model with factory
            val taskViewModel: TaskViewModel = viewModel(
                factory = TaskViewModelFactory(taskRepository, userPreferencesRepository)
            )

            // Get the user preferences for theme
            val userPreferences by taskViewModel.userPreferences.collectAsState(
                initial = com.uts.studentdo.data.UserPreferences(
                    darkTheme = isSystemInDarkTheme(),
                    sortOrder = "date",
                    priorityFilter = 0
                )
            )

            StudentTaskManagerTheme(darkTheme = userPreferences.darkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavHost(taskViewModel = taskViewModel)
                }
            }
        }
    }
}