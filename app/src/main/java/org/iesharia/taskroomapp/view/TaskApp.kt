package org.iesharia.taskroomapp.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.iesharia.taskroomapp.model.AppDatabase
import org.iesharia.taskroomapp.model.Task
import org.iesharia.taskroomapp.model.TaskType

@Composable
fun TaskApp(database: AppDatabase) {
    val taskDao = database.taskDao()
    val scope = rememberCoroutineScope()

    // Variables de estado para manejar datos y entradas del usuario
    var tasks by remember { mutableStateOf(listOf<Task>()) }
    var taskTypes by remember { mutableStateOf(listOf<TaskType>()) }
    var newTaskName by remember { mutableStateOf("") }
    var newTaskTypeName by remember { mutableStateOf("") }
    var selectedTaskTypeId by remember { mutableStateOf<Int?>(null) }
    var newTaskDescription by remember { mutableStateOf("") }
    var showDescription by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Cargar tareas y tipos de tareas al iniciar
    LaunchedEffect(Unit) {
        tasks = taskDao.getAllTasks()
        taskTypes = taskDao.getAllTaskTypes()
    }

    // Contenedor principal
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB6D8C1))
            .padding(16.dp)
    ) {
        // Título de la pantalla
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

        // Entrada para añadir tareas
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
                modifier = Modifier.weight(0.6f),
                value = if (selectedTaskTypeId != null) selectedTaskTypeId.toString() else "",
                onValueChange = {},
                enabled = false,
                label = { Text("Tipo") },
                shape = RoundedCornerShape(20.dp),
                textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 22.sp, color = Color.Black),
                colors = TextFieldDefaults.colors(focusedContainerColor = Color(0xFFCFF0D9))
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    if (newTaskName.isNotBlank() && selectedTaskTypeId != null) {
                        scope.launch(Dispatchers.IO) {
                            val newTask = Task(name = newTaskName, description = newTaskDescription, taskTypeId = selectedTaskTypeId!!)
                            taskDao.insert(newTask)
                            tasks = taskDao.getAllTasks()
                            newTaskName = ""
                            newTaskDescription = ""
                        }
                    } else {
                        Toast.makeText(context, "Introduce un nombre y selecciona un tipo", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.size(50.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color(0xFF598D61),
                    modifier = Modifier.size(50.dp)
                )
            }
        }

        // Entrada para añadir una descripción opcional
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
        ) {
            if (showDescription) {
                OutlinedTextField(
                    value = newTaskDescription,
                    onValueChange = { newTaskDescription = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFCFF0D9),
                        unfocusedContainerColor = Color(0xFFCFF0D9)
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = if (showDescription) "Ocultar" else "Añade una descripción",
                fontWeight = FontWeight(500),
                fontSize = 17.sp,
                color = Color.White,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF3A83BD))
                    .clickable { showDescription = !showDescription }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }

        // Entrada para añadir tipos de tareas
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
                    var titleExists = false
                    taskTypes.forEach {
                        if (it.title.lowercase() == newTaskTypeName) {
                            titleExists = true
                        }
                    }
                    if (newTaskTypeName.isNotBlank() && !titleExists) {
                        scope.launch(Dispatchers.IO) {
                            val newTaskType = TaskType(title = newTaskTypeName)
                            taskDao.insertTaskType(newTaskType)
                            taskTypes = taskDao.getAllTaskTypes()
                            newTaskTypeName = ""
                        }
                    } else {
                        Toast.makeText(context,
                            if (titleExists) "El tipo de tarea ya existe" else "Introduce un tipo válido", Toast.LENGTH_SHORT).show()
                    }
                }, modifier = Modifier.size(50.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color(0xFF598D61),
                    modifier = Modifier.size(50.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar contenido (tareas y tipos)
        ShowContent(
            database = database,
            tasks = tasks,
            taskTypes = taskTypes,
            onUpdateTasks = { tasks = it },
            onUpdateTaskTypes = { taskTypes = it },
            onTaskTypeSelected = { selectedTaskTypeId = it?.id },
            selectedTaskTypeId = selectedTaskTypeId
        )
    }
}