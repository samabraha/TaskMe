@file:OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)

package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import model.Note
import vm.NoteAction
import vm.NoteViewModel
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NoteHomeUI(noteViewModel: NoteViewModel, modifier: Modifier = Modifier) {
    val notes = noteViewModel.notes
    var selectedNote by remember { mutableStateOf<Note?>(null) }

    Row(modifier = modifier.fillMaxSize()) {
        NoteList(
            notes = notes,
            selectNote = { note -> selectedNote = if (selectedNote == note) null else note },
            modifier = Modifier.fillMaxWidth(0.5f)
        )

        selectedNote?.let {
            EditableNote(
                note = it,
                addNoteAction = { action ->
                    noteViewModel.handleNoteAction(action)
                })
        }

        FloatingActionButton(
            onClick = { selectedNote = Note(title = "", content = "", category = "") }) {
            Icon(Icons.Rounded.Add, contentDescription = "Add Note")
        }
    }
}


@Composable
fun NoteList(notes: List<Note>, selectNote: (Note) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        items(notes) { note ->
            NoteView(note = note, selectNote = selectNote)
        }
    }
}


@Composable
fun NoteView(note: Note, selectNote: (Note) -> Unit, modifier: Modifier = Modifier) {
    val titleStyle = MaterialTheme.typography.titleLarge.copy(
        fontWeight = FontWeight.Bold,
        shadow = Shadow(color = Color.Black, offset = Offset(1f, 1f), blurRadius = 2f)
    )

    MaterialTheme.typography.bodyLarge
    val smallTextStyle = MaterialTheme.typography.bodySmall

    Card(
        modifier = modifier.padding(horizontal = 6.dp, vertical = 3.dp).fillMaxSize().padding(4.dp),
        onClick = { selectNote(note) }) {
        Text(
            text = note.title,
            style = titleStyle,
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier.background(note.color).fillMaxWidth().padding(5.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Text(
                text = "Created At: ${Instant.fromEpochMilliseconds(note.createdAt)}",
                style = smallTextStyle
            )
            Text(text = "Note Type: ${note.noteType}")
        }
    }
}

@Composable
fun EditableNote(note: Note, addNoteAction: (NoteAction) -> Unit, modifier: Modifier = Modifier) {
    var title by remember { mutableStateOf(note.title) }
    var content by remember { mutableStateOf(note.content) }
    var category by remember { mutableStateOf(note.category) }

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

    val titleStyle = MaterialTheme.typography.titleLarge.copy(
        color = Color.White,
        fontWeight = FontWeight.Bold,
        shadow = Shadow(color = Color.Black, offset = Offset(1f, 1f), blurRadius = 2f)
    )
    val contentStyle = MaterialTheme.typography.bodyLarge

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        TextInputBox(
            onValueChange = { title = it },
            value = title,
            textStyle = titleStyle,
            backColor = note.color,
        )
        TextInputBox(
            onValueChange = { content = it }, value = content, textStyle = contentStyle,
            backColor = note.color.copy(alpha = .25f), singleLine = false
        )
        TextInputBox(onValueChange = { category = it }, value = category, textStyle = contentStyle)

        Text(text = "Note Type: ${note.noteType}", style = contentStyle)
        Text(
            text = "Created At: ${Instant.fromEpochMilliseconds(note.createdAt)}",
            style = contentStyle
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
fun TextInputBox(
    value: String,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle,
    singleLine: Boolean = true,
    backColor: Color = Color.Transparent,
    modifier: Modifier = Modifier
) {
    TextField(
        singleLine = singleLine,
        value = value,
        onValueChange = { onValueChange(it) },
        textStyle = textStyle,
        modifier = modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = backColor, unfocusedContainerColor = backColor
        )
    )
}

