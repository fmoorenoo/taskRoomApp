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
fun ShowTaskTypes(
    database: AppDatabase,
    taskTypes: List<TaskType>,
    onUpdateTaskTypes: (List<TaskType>) -> Unit,
    onUpdateTasks: (List<Task>) -> Unit,
    onTaskTypeSelected: (TaskType?) -> Unit,
    selectedTaskTypeId: Int?
) {
    val taskDao = database.taskDao()
    val taskTypeDao = database.taskTypeDao()
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxWidth()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            modifier = Modifier.fillMaxWidth().heightIn(0.dp, 165.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(taskTypes) { taskType ->
                TaskTypeCard(
                    task_type = taskType,
                    onDelete = {
                        scope.launch(Dispatchers.IO) {
                            taskTypeDao.deleteTaskType(taskType)
                            onUpdateTaskTypes(taskTypeDao.getAllTaskTypes())
                            onUpdateTasks(taskDao.getAllTasks())
                        }
                    },
                    onEdit = { newTitle ->
                        scope.launch(Dispatchers.IO) {
                            val updatedTaskType = taskType.copy(title = newTitle)
                            taskTypeDao.updateTaskType(updatedTaskType)
                            onUpdateTaskTypes(taskTypeDao.getAllTaskTypes())
                        }
                    },
                    isSelected = selectedTaskTypeId == taskType.id,
                    onSelect = { onTaskTypeSelected(taskType) }
                )
            }
        }
    }
}