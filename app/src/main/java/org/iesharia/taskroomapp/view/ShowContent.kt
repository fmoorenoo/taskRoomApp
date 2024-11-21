package org.iesharia.taskroomapp.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.iesharia.taskroomapp.AppDatabase
import org.iesharia.taskroomapp.Task
import org.iesharia.taskroomapp.TaskType

@Composable
fun ShowContent(
    database: AppDatabase,
    tasks: List<Task>,
    taskTypes: List<TaskType>,
    onUpdateTasks: (List<Task>) -> Unit,
    onUpdateTaskTypes: (List<TaskType>) -> Unit
) {
    val taskDao = database.taskDao()
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxWidth()) {
        // Lista de tipos de tareas
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(taskTypes) { taskType ->
                TaskTypeCard(
                    task_type = taskType,
                    onDelete = {
                        scope.launch(Dispatchers.IO) {
                            taskDao.deleteTaskType(taskType)
                            onUpdateTaskTypes(taskDao.getAllTaskTypes())
                        }
                    },
                    onEdit = { newTitle ->
                        scope.launch(Dispatchers.IO) {
                            val updatedTaskType = taskType.copy(title = newTitle)
                            taskDao.updateTaskType(updatedTaskType)
                            onUpdateTaskTypes(taskDao.getAllTaskTypes())
                        }
                    }
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(tasks) { task ->
                TaskCard(
                    task = task,
                    taskTypes = taskTypes,
                    onDelete = {
                        scope.launch(Dispatchers.IO) {
                            taskDao.delete(task)
                            onUpdateTasks(taskDao.getAllTasks())
                        }
                    },
                    onEdit = { newName ->
                        scope.launch(Dispatchers.IO) {
                            val updatedTask = task.copy(name = newName)
                            taskDao.update(updatedTask)
                            onUpdateTasks(taskDao.getAllTasks())
                        }
                    }
                )
            }
        }
    }
}
