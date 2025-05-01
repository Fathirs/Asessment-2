package com.uts.studentdo

import android.app.Application
import com.uts.studentdo.data.AppDatabase
import com.uts.studentdo.data.TaskRepository
import com.uts.studentdo.data.UserPreferencesRepository

class TaskManagerApplication : Application() {

    // Lazy initialize the database and repositories so they're only created when needed
    val database by lazy { AppDatabase.getDatabase(this) }
    val taskRepository by lazy { TaskRepository(database.taskDao()) }
    val userPreferencesRepository by lazy { UserPreferencesRepository(this) }
}