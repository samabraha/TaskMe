@file:OptIn(ExperimentalUuidApi::class)

package ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import model.Note
import vm.NoteAction
import java.time.Instant
import kotlin.uuid.ExperimentalUuidApi

@Composable
fun EditableNote(note: Note, addNoteAction: (NoteAction) -> Unit, modifier: Modifier = Modifier) {
    var title by remember(note.id) { mutableStateOf(note.title) }
    var content by remember(note.id) { mutableStateOf(note.content) }
    var category by remember(note.id) { mutableStateOf(note.category) }

    val titleStyle = MaterialTheme.typography.titleLarge.copy(
        color = Color.White,
        fontWeight = FontWeight.Bold,
        shadow = Shadow(color = Color.Black, offset = Offset(1f, 1f), blurRadius = 2f)
    )

    val contentStyle = MaterialTheme.typography.bodyLarge

    val fields = listOf(
        InputField(value = title, onValueChange = { title = it }, textStyle = titleStyle, backgroundColor = note.color),
        InputField(
            value = content,
            onValueChange = { content = it },
            textStyle = contentStyle,
            backgroundColor = note.color.copy(alpha = 0.5f)
        ),
        InputField(
            value = category,
            onValueChange = { category = it },
            textStyle = contentStyle,
            backgroundColor = note.color.copy(alpha = 0.5f)
        ),
    )

    val saveNote: () -> Unit = {
        val newNote = note.let {
            Note(
                id = it.id,
                title = title,
                content = content,
                category = category,
                createdAt = it.createdAt,
                noteType = it.noteType,
                color = it.color
            )
        }

        addNoteAction(NoteAction.AddNote(newNote))
    }

    val deleteNote: () -> Unit = {
        addNoteAction(NoteAction.DeleteNote(note.id))
    }

    Column(
        modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween
    ) {
        TextInputBoxes(fields)

        Text(text = "Note Type: ${note.noteType}", style = contentStyle)
        Text(
            text = dateTimeFormatter.format(Instant.ofEpochMilli(note.createdAt)), style = contentStyle
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