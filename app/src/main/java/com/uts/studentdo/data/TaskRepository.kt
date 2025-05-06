package com.uts.studentdo.data

import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    // Get all tasks
    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()

    // Get deleted tasks (recycle bin)
    val deletedTasks: Flow<List<DeletedTask>> = taskDao.getDeletedTasks()

    // Get a specific task
    suspend fun getTaskById(taskId: Long): Task? {
        return taskDao.getTaskById(taskId)
    }

    // Insert a new task
    suspend fun insertTask(task: Task): Long {
        return taskDao.insertTask(task)
    }

    // Update an existing task
    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    // Move a task to recycle bin
    suspend fun moveTaskToRecycleBin(task: Task) {
        // First, insert into deleted_tasks
        val deletedTask = DeletedTask(
            id = task.id,
            title = task.title,
            description = task.description,
            dueDate = task.dueDate,
            priority = task.priority
        )
        
        // Insert into deleted_tasks table
        taskDao.insertDeletedTask(deletedTask)

        // Then delete from tasks
        taskDao.deleteTask(task)
    }

    // Permanently delete a task (from recycle bin)
    suspend fun permanentlyDeleteTask(taskId: Long) {
        taskDao.removeFromDeletedTasks(taskId)
    }

    // Restore task from recycle bin
    suspend fun restoreTaskFromRecycleBin(deletedTask: DeletedTask) {
        // Create a Task from DeletedTask
        val task = Task(
            id = deletedTask.id,
            title = deletedTask.title,
            description = deletedTask.description,
            dueDate = deletedTask.dueDate,
            priority = deletedTask.priority
        )

        // Insert back to tasks table
        taskDao.insertTask(task)

        // Remove from deleted_tasks
        taskDao.removeFromDeletedTasks(deletedTask.id)
    }
}