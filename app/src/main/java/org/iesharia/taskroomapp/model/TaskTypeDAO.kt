package org.iesharia.taskroomapp.model

import androidx.room.*

@Dao interface TaskTypeDao {
    // CRUD para 'TaskType'
    @Insert
    suspend fun insertTaskType(taskType: TaskType)

    @Query("SELECT * FROM task_type")
    suspend fun getAllTaskTypes(): List<TaskType>

    @Update
    suspend fun updateTaskType(taskType: TaskType)

    @Delete
    suspend fun deleteTaskType(taskType: TaskType)

    @Query("SELECT * FROM task_type WHERE id = :id")
    fun getTaskTypeById(id: Int): TaskType?
}