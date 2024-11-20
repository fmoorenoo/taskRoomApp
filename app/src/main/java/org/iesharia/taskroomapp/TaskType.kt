package org.iesharia.taskroomapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_type")
data class TaskType(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String
)