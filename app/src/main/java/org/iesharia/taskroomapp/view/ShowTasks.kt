package org.iesharia.taskroomapp.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.iesharia.taskroomapp.model.AppDatabase
import org.iesharia.taskroomapp.model.Task
import org.iesharia.taskroomapp.model.TaskType

@Composable
fun ShowTasks(
    database: AppDatabase,
    tasks: List<Task>,
    taskTypes: List<TaskType>,
    onUpdateTasks: (List<Task>) -> Unit
) {
    val taskDao = database.taskDao()
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxWidth()) {
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

