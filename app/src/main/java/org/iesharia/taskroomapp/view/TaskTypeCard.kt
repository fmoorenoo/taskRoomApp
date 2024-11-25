package org.iesharia.taskroomapp.view

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.iesharia.taskroomapp.model.TaskType

@Composable
fun TaskTypeCard(task_type: TaskType, onDelete: () -> Unit, onEdit: (String) -> Unit, isSelected: Boolean, onSelect: () -> Unit) {
    var name: String = task_type.title
    var edit by remember { mutableStateOf(false) }
    var editText by remember { mutableStateOf(name) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .border(width = 2.dp,
                shape = RoundedCornerShape(5.dp),
                color = if (isSelected) Color(0xFF6BCC8A) else Color.Unspecified),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFB6F4CA) else Color(0xFFCFF0D9)
        ),
        shape = RoundedCornerShape(5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = Modifier.padding(start = 8.dp)) {
                if (edit) {
                    OutlinedTextField(
                        value = editText,
                        onValueChange = { editText = it },
                        label = { Text("Edit title") },
                        modifier = Modifier.padding(bottom = 5.dp, start = 5.dp)
                    )
                } else {
                    if (task_type.title.length > 13) {
                        name = task_type.title.substring(0, 13) + "..."
                    }
                    Text(
                        text = name,
                        fontSize = 18.sp,
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.End) {
                if (edit) {
                    IconButton(onClick = {
                        onEdit(editText)
                        edit = false
                    }) {
                        Icon(Icons.Default.Check, contentDescription = "Save", tint = Color(0xFF4CAF50))
                    }

                    IconButton(onClick = {
                        edit = false
                        editText = task_type.title
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Cancel", tint = Color(0xFFFF5722))
                    }
                } else {
                    IconButton(onClick = { edit = true }) {
                        Icon(Icons.Default.Create, contentDescription = "Edit", tint = Color(0xFF3871AB))
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFB93E3E))
                    }
            }
        }
        }
    }
}
