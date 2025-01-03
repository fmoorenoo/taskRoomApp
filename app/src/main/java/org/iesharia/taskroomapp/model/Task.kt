package org.iesharia.taskroomapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "tasks",
    // Fuente: https://stackoverflow.com/questions/47511750/how-to-use-foreign-key-in-room-persistence-library
    foreignKeys = [
        ForeignKey(
            entity = TaskType::class,
            // Referencia en la tabla TaskType
            parentColumns = ["id"],
            // Referencia en esta tabla
            childColumns = ["taskTypeId"],
            // Si se borra un tipo de tarea, deben borrarse sus tareas asociadas
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String = "",
    val taskTypeId: Int
)
