package com.uts.studentdo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deleted_tasks")
data class DeletedTask(
    @PrimaryKey
    val id: Long,
    val title: String,
    val description: String,
    val dueDate: Long,
    val priority: Int,
    val deletedAt: Long = System.currentTimeMillis() // Timestamp when the task was deleted
)