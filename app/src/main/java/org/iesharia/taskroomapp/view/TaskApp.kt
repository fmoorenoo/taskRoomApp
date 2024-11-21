package org.iesharia.taskroomapp.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.iesharia.taskroomapp.AppDatabase
import org.iesharia.taskroomapp.Task
import org.iesharia.taskroomapp.TaskType

@Composable
fun TaskApp(database: AppDatabase) {
    val taskDao = database.taskDao()
    val scope = rememberCoroutineScope()
    var tasks by remember { mutableStateOf(listOf<Task>()) }
    var task_types by remember { mutableStateOf(listOf<TaskType>()) }
    var newTaskName by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Cargar tareas y tipos de tareas al iniciar
    LaunchedEffect(Unit) {
        tasks = taskDao.getAllTasks()
        task_types = taskDao.getAllTaskTypes()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB6D8C1))
            .padding(16.dp)
    ) {
        Box(modifier = Modifier.padding(vertical = 15.dp, horizontal = 5.dp)) {
            Text(
                text = "Lista de tareas",
                fontWeight = FontWeight.Bold,
                fontSize = 27.sp,
                color = Color.White,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(15.dp))
                    .background(Color(0xFF598D61))
                    .padding(horizontal = 15.dp, vertical = 7.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
        ) {
            OutlinedTextField(
                value = newTaskName,
                onValueChange = { newTaskName = it },
                label = { Text("Nueva tarea") },
                modifier = Modifier
                    .weight(1f),
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFCFF0D9),
                    unfocusedContainerColor = Color(0xFFCFF0D9)
                )
            )

            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    if (newTaskName.isNotBlank()) {
                        scope.launch(Dispatchers.IO) {
                            val newTask = Task(name = newTaskName, description = "d", taskTypeId = 1)
                            taskDao.insert(newTask)
                            tasks = taskDao.getAllTasks()
                            newTaskName = ""
                        }
                    } else {
                        Toast.makeText(context, "Introduce un nombre válido", Toast.LENGTH_SHORT).show()
                    }
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.fillMaxWidth()){
            // Lista de tipos de tareas
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(task_types) { task_type ->
                    TaskTypeCard(
                        task_type = task_type,
                        onDelete = {
                            scope.launch(Dispatchers.IO) {
                                taskDao.deleteTaskType(task_type)
                                task_types = taskDao.getAllTaskTypes()
                            }
                        },
                        onEdit = { newTitle ->
                            scope.launch(Dispatchers.IO) {
                                val updatedTaskType = task_type.copy(title = newTitle)
                                taskDao.updateTaskType(updatedTaskType)
                                task_types = taskDao.getAllTaskTypes()
                            }
                        }
                    )
                }
            }


            // Lista de tareas
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(tasks) { task ->
                    TaskCard(
                        task = task,
                        onDelete = {
                            scope.launch(Dispatchers.IO) {
                                taskDao.delete(task)
                                tasks = taskDao.getAllTasks()
                            }
                        },
                        onEdit = { newName ->
                            scope.launch(Dispatchers.IO) {
                                val updatedTask = task.copy(name = newName)
                                taskDao.update(updatedTask)
                                tasks = taskDao.getAllTasks()
                            }
                        }
                    )
                }
            }
        }

    }
}