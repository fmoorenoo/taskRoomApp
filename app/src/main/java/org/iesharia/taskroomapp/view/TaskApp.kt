package org.iesharia.taskroomapp.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
    var taskTypes by remember { mutableStateOf(listOf<TaskType>()) }
    var newTaskName by remember { mutableStateOf("") }
    var newTaskTypeName by remember { mutableStateOf("") }
    var taskTypeID by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Cargar tareas y tipos de tareas al iniciar
    LaunchedEffect(Unit) {
        tasks = taskDao.getAllTasks()
        taskTypes = taskDao.getAllTaskTypes()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB6D8C1))
            .padding(16.dp)
    ) {
        Box(modifier = Modifier.padding(vertical = 15.dp, horizontal = 5.dp)) {
            Text(
                text = "Inicio",
                fontWeight = FontWeight.Bold,
                fontSize = 27.sp,
                color = Color.White,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(15.dp))
                    .background(Color(0xFF598D61))
                    .padding(horizontal = 15.dp, vertical = 7.dp)
            )
        }
        // Input para añadir nueva tarea
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
        ) {
            OutlinedTextField(
                value = newTaskName,
                onValueChange = { newTaskName = it },
                label = { Text("Nueva tarea") },
                modifier = Modifier.weight(2f).padding(end = 5.dp),
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFCFF0D9),
                    unfocusedContainerColor = Color(0xFFCFF0D9)
                )
            )
            OutlinedTextField(
                value = taskTypeID,
                onValueChange = { taskTypeID = it },
                label = { Text("Tipo(ID)") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFCFF0D9),
                    unfocusedContainerColor = Color(0xFFCFF0D9)
                )
            )

            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    if (newTaskName.isNotBlank() && taskTypeID.isNotBlank()) {
                        scope.launch(Dispatchers.IO) {
                            val taskTypeExists = taskDao.getTaskTypeById(taskTypeID.toInt()) != null
                            if (taskTypeExists) {
                                val newTask = Task(name = newTaskName, description = "d", taskTypeId = taskTypeID.toInt())
                                taskDao.insert(newTask)
                                tasks = taskDao.getAllTasks()
                                Log.i("Dam2", "$tasks")
                                newTaskName = ""
                                taskTypeID = ""
                            } else {
                                scope.launch(Dispatchers.Main) {
                                    Toast.makeText(context, "ID de tipo de tarea no válido", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    } else {
                        Toast.makeText(context, "Introduce un nombre y un ID válidos", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }

        }

        // Input para añadir nuevo tipo de tarea
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
        ) {
            OutlinedTextField(
                value = newTaskTypeName,
                onValueChange = { newTaskTypeName = it },
                label = { Text("Nuevo tipo de tarea") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFCFF0D9),
                    unfocusedContainerColor = Color(0xFFCFF0D9)
                )
            )

            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    if (newTaskTypeName.isNotBlank()) {
                        scope.launch(Dispatchers.IO) {
                            val newTaskType = TaskType(title = newTaskTypeName)
                            taskDao.insertTaskType(newTaskType)
                            taskTypes = taskDao.getAllTaskTypes()
                            newTaskTypeName = ""
                        }
                    } else {
                        Toast.makeText(context, "Introduce un nombre válido", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar tipos de tareas y tareas
        ShowContent(
            database = database,
            tasks = tasks,
            taskTypes = taskTypes,
            onUpdateTasks = { tasks = it },
            onUpdateTaskTypes = { taskTypes = it }
        )
    }
}
