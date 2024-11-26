package org.iesharia.taskroomapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.iesharia.taskroomapp.model.Task
import org.iesharia.taskroomapp.model.TaskType

@Composable
fun TaskCard(
    task: Task,
    taskTypes: List<TaskType>,
    onDelete: () -> Unit, onEdit: (String) -> Unit) {
    var name: String = task.name
    var edit by remember { mutableStateOf(false) }
    var editText by remember { mutableStateOf(name) }
    var showDescription by remember { mutableStateOf(false) }

    // Buscar el tipo de tarea mediante el ID
    val searchTaskType = taskTypes.find { it.id == task.taskTypeId }
    // Si encuentra el id, almacena el título correspondiente. Si no, se le asigna 'Sin tipo'
    val taskTypeTitle = if (searchTaskType != null) {searchTaskType.title} else {"Sin tipo"}
    var typeName: String = taskTypeTitle

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFDAF4E2)
        ),
        shape = RoundedCornerShape(5.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (taskTypeTitle.length > 8) {
                    typeName = taskTypeTitle.substring(0, 8) + "..."
                }
                Text(
                    text = typeName,
                    fontWeight = FontWeight(500),
                    fontSize = 17.sp,
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(6.dp))
                        .background(Color(0xFF83E4A4))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )

                Text(
                    text = "ID-${task.id}",
                    fontWeight = FontWeight(500),
                    fontSize = 17.sp
                )
            }

            if (edit) {
                OutlinedTextField(
                    value = editText,
                    singleLine = true,
                    onValueChange = { editText = it },
                    label = { Text("Task Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                if (task.name.length > 17) {
                    name = task.name.substring(0, 17) + "..."
                }

                Text(
                    text = name,
                    modifier = Modifier.padding(4.dp).padding(top = 5.dp),
                    fontSize = 17.sp
                )
            }

            if (showDescription) {
                Text(
                    text = task.description,
                    modifier = Modifier.padding(4.dp),
                    fontSize = 15.sp,
                    color = Color.Gray
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.fillMaxSize()
            ) {
                if (edit) {
                    IconButton(onClick = {
                        onEdit(editText)
                        edit = false
                    }) {
                        Icon(Icons.Default.Check, contentDescription = "Save", tint = Color(0xFF4CAF50))
                    }

                    IconButton(onClick = {
                        edit = false
                        editText = task.name
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Cancel", tint = Color(0xFFFF5722))
                    }
                } else {
                    IconButton(onClick = { edit = true }) {
                        Icon(Icons.Default.Create, contentDescription = "Edit", tint = Color(0xFF3871AB))
                    }
                    if ((task.description).isNotBlank()) {
                        IconButton(onClick = { showDescription = !showDescription }) {
                            Icon(
                                imageVector = if (showDescription) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = if (showDescription) "Hide Description" else "Show Description",
                                tint = Color.Black
                            )
                        }
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFB93E3E))
                    }
                }
            }
        }
    }
}

