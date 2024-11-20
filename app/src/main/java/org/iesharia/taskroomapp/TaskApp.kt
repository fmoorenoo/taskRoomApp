package org.iesharia.taskroomapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun TaskApp(database: AppDatabase) {
    val taskDao = database.taskDao()
    val scope = rememberCoroutineScope()
    var tasks by remember { mutableStateOf(listOf<Task>()) }
    var newTaskName by remember { mutableStateOf("") }

    // Cargar tareas al iniciar
    LaunchedEffect(Unit) {
        tasks = taskDao.getAllTasks()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Lista de tareas",
            modifier = Modifier.padding(bottom = 17.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = newTaskName,
                onValueChange = { newTaskName = it },
                label = { Text("Nueva tarea") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (newTaskName.isNotBlank()) {
                        scope.launch(Dispatchers.IO) {
                            val newTask = Task(name = newTaskName)
                            taskDao.insert(newTask)
                            tasks = taskDao.getAllTasks()
                            newTaskName = ""
                        }
                    }
                },
                modifier = Modifier.size(56.dp)
            ) {
                Text("+")
            }

            // Mostrar lista de tareas
            tasks.forEach { task ->
                Text(text = task.name)
            }
        }
    }
}
