package com.uts.studentdo.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY dueDate ASC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: Long): Task?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    // For the recycle bin feature
    @Query("SELECT * FROM deleted_tasks")
    fun getDeletedTasks(): Flow<List<DeletedTask>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeletedTask(deletedTask: DeletedTask)

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: Long)

    // Restore from recycle bin
    @Query("DELETE FROM deleted_tasks WHERE id = :taskId")
    suspend fun removeFromDeletedTasks(taskId: Long)
}