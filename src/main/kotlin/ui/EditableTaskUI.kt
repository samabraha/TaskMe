@file:OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)

package ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import kotlinx.serialization.json.Json
import model.Task
import vm.TaskAction
import java.time.Instant
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaInstant
import kotlin.uuid.ExperimentalUuidApi

@Composable
fun EditableNote(task: Task, addNoteAction: (TaskAction) -> Unit, modifier: Modifier = Modifier) {
    var title by remember(task.id) { mutableStateOf(task.title) }
    var content by remember(task.id) { mutableStateOf(task.content) }
    var category by remember(task.id) { mutableStateOf(task.category) }
    var tags by remember(task.id) { mutableStateOf(Json.encodeToString<Set<String>>(task.tags)) }

    val titleStyle = MaterialTheme.typography.titleLarge.copy(
        color = Color.White,
        fontWeight = FontWeight.Bold,
        shadow = Shadow(color = Color.Black, offset = Offset(1f, 1f), blurRadius = 2f)
    )

    val contentStyle = MaterialTheme.typography.bodyLarge

    val fields = listOf(
        InputField(value = title, onValueChange = { title = it }, textStyle = titleStyle, backgroundColor = task.color),
        InputField(
            value = content,
            onValueChange = { content = it },
            textStyle = contentStyle,
            singleLine = false,
            backgroundColor = task.color.copy(alpha = 0.5f)
        ),
        InputField(
            value = category,
            onValueChange = { category = it },
            textStyle = contentStyle,
            backgroundColor = task.color.copy(alpha = 0.5f)
        ),
        InputField(
            value = tags,
            onValueChange = { tags = it }, textStyle = contentStyle
        )
    )

    val saveNote: () -> Unit = {
        val newTask = task.let {
            Task(
                id = it.id,
                title = title,
                content = content,
                category = category,
                createdAt = it.createdAt,
                taskType = it.taskType,
                tags = Json.decodeFromString(tags),
                color = it.color
            )
        }

        addNoteAction(TaskAction.AddTask(newTask))
    }

    val deleteNote: () -> Unit = {
        addNoteAction(TaskAction.DeleteTask(task.id))
    }

    Column(
        modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween
    ) {
        TextInputBoxes(fields)

        Text(text = "Note Type: ${task.taskType}", style = contentStyle)
        Text(
            text = dateTimeFormatter.format(task.createdAt.toJavaInstant()), style = contentStyle
        )
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ElevatedButton(onClick = {
                if (title.isNotBlank() && content.isNotBlank()) {
                    deleteNote()
                }
            }) { Text(text = "Delete Note") }

            if (title.isNotBlank() && content.isNotBlank()) {
                ElevatedButton(onClick = {
                    saveNote()
                }) { Text(text = "Save Note") }
            }
        }
    }
}

@Composable
fun TextInputBoxes(fields: List<InputField>) {
    fields.forEach { field ->
        TextInputBox(field)
    }
}

@Composable
fun TextInputBox(
    field: InputField, modifier: Modifier = Modifier
) {
    TextField(
        singleLine = field.singleLine,
        value = field.value,
        onValueChange = field.onValueChange,
        textStyle = field.textStyle ?: MaterialTheme.typography.titleLarge,
        modifier = modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = field.backgroundColor, unfocusedContainerColor = field.backgroundColor
        )
    )
}

data class InputField(
    val value: String,
    val onValueChange: (String) -> Unit,
    val label: String? = null,
    val textStyle: TextStyle? = null,
    val singleLine: Boolean = true,
    val backgroundColor: Color = Color.Transparent

)