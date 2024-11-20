package org.iesharia.taskroomapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Task::class, TaskType::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "task_database"
                )
                    // Si cambia la versión de la base de datos, esta se elimina y se recrea con las modificaciones.
                    .fallbackToDestructiveMigration()
                    // Esto hace que se pierdan los datos, pero es provisional mientras construyo la app
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
