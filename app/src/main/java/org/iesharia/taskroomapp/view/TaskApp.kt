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
import androidx.compose.ui.text.font.FontWeight
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
    val taskTypeDao = database.taskTypeDao()
    val scope = rememberCoroutineScope()

    // Variables de estado para manejar datos y entradas del usuario
    var tasks by remember { mutableStateOf(listOf<Task>()) }
    var taskTypes by remember { mutableStateOf(listOf<TaskType>()) }
    var newTaskName by remember { mutableStateOf("") }
    var newTaskTypeName by remember { mutableStateOf("") }
    var selectedTaskTypeId by remember { mutableStateOf<Int?>(null) }
    var newTaskDescription by remember { mutableStateOf("") }
    var showNewTypeField by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Cargar tareas y tipos de tareas al iniciar
    LaunchedEffect(Unit) {
        tasks = taskDao.getAllTasks()
        taskTypes = taskTypeDao.getAllTaskTypes()
    }

    // Contenedor principal
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB6D8C1))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Card para añadir tareas
        Card(
            modifier = Modifier.fillMaxWidth().padding(5.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF8BC99C)),
            shape = RoundedCornerShape(15.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título
                Text(
                    text = "Crear nueva tarea",
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Nombre de la tarea
                OutlinedTextField(
                    value = newTaskName,
                    onValueChange = { newTaskName = it },
                    label = { Text("Nombre de la tarea") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(15.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFCFF0D9),
                        unfocusedContainerColor = Color(0xFFCFF0D9),
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Descripción
                OutlinedTextField(
                    value = newTaskDescription,
                    onValueChange = { newTaskDescription = it },
                    label = { Text("Descripción (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(15.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFCFF0D9),
                        unfocusedContainerColor = Color(0xFFCFF0D9)
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Tipos de tareas
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Selecciona un tipo",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = Color.Black
                    )
                    Text(
                        text = if (showNewTypeField) "Cancelar" else "Añadir +",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier.clickable { showNewTypeField = !showNewTypeField }.padding(4.dp)
                    )
                }

                // Mostrar tipos de tareas existentes
                ShowTaskTypes(
                    database = database,
                    taskTypes = taskTypes,
                    onUpdateTasks = { tasks = it },
                    onUpdateTaskTypes = { taskTypes = it },
                    onTaskTypeSelected = { selectedTaskTypeId = it?.id },
                    selectedTaskTypeId = selectedTaskTypeId
                )

                // Añadir nuevo tipo de tarea
                if (showNewTypeField) {
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp)
                    ) {
                        OutlinedTextField(
                            value = newTaskTypeName,
                            singleLine = true,
                            onValueChange = { newTaskTypeName = it },
                            label = { Text("Nuevo tipo de tarea") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(5.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFCFF0D9),
                                unfocusedContainerColor = Color(0xFFCFF0D9)
                            )
                        )
                        IconButton(
                            onClick = {
                                var titleExists = false
                                taskTypes.forEach {
                                    if (it.title.lowercase() == newTaskTypeName.lowercase()) {
                                        titleExists = true
                                    }
                                }
                                if (newTaskTypeName.isNotBlank() && !titleExists) {
                                    scope.launch(Dispatchers.IO) {
                                        val newTaskType = TaskType(title = newTaskTypeName)
                                        taskTypeDao.insertTaskType(newTaskType)
                                        taskTypes = taskTypeDao.getAllTaskTypes()
                                        newTaskTypeName = ""
                                        showNewTypeField = !showNewTypeField
                                    }
                                } else {
                                    Toast.makeText(context,
                                        if (titleExists) "El tipo ya existe" else "Introduce un tipo válido", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.size(50.dp).padding(top = 5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Añadir tipo de tarea",
                                tint = Color(0xFF2D65A4),
                                modifier = Modifier.size(50.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Botón para añadir la tarea
                Button(
                    onClick = {
                        if (newTaskName.isNotBlank() && selectedTaskTypeId != null) {
                            scope.launch(Dispatchers.IO) {
                                val newTask = Task(name = newTaskName, description = newTaskDescription, taskTypeId = selectedTaskTypeId!!)
                                taskDao.insert(newTask)
                                tasks = taskDao.getAllTasks()
                                newTaskName = ""
                                newTaskDescription = ""
                            }
                            scope.launch(Dispatchers.Main) {
                                Toast.makeText(context, "Tarea añadida con éxito", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Indica un nombre y un tipo", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2788DC))
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Añadir tarea",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Añadir tarea", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.padding(vertical = 15.dp, horizontal = 5.dp)) {
                Text(
                    text = "Tareas",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(15.dp))
                        .background(Color(0xFF598D61))
                        .padding(horizontal = 15.dp, vertical = 7.dp)
                )
            }
        }

        // Mostrar tareas
        ShowTasks (
            database = database,
            tasks = tasks,
            taskTypes = taskTypes,
            onUpdateTasks = { tasks = it },
        )
    }
}