package org.iesharia.taskroomapp.model

import androidx.room.*

@Dao interface TaskDao {
    // CRUD para 'Task'
    @Insert
    suspend fun insert(task: Task)

    @Query("SELECT * FROM tasks")
    suspend fun getAllTasks(): List<Task>

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)
}