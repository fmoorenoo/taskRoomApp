package org.iesharia.taskroomapp

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


// CRUD para 'TaskType'
    @Insert
    suspend fun insertTaskType(taskType: TaskType)

    @Query("SELECT * FROM task_type")
    suspend fun getAllTaskTypes(): List<TaskType>

    @Update
    suspend fun updateTaskType(taskType: TaskType)

    @Delete
    suspend fun deleteTaskType(taskType: TaskType)
}