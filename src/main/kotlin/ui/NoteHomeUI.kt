package ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow
import model.Note
import vm.NoteAction
import vm.NoteViewModel
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NoteHomeUI(noteViewModel: NoteViewModel, modifier: Modifier = Modifier) {
    val notes = noteViewModel.notes
    var showDialog by remember { mutableStateOf(false) }

    var selectedNote by remember { mutableStateOf<Note?>(null) }

    Column(modifier = modifier) {
        Row {
            NoteList(notes = notes, selectNote = { note ->
                selectedNote = note
                showDialog = true
            })

            FloatingActionButton(onClick = {
                selectedNote = null
                showDialog = true
            }) {
                Icon(Icons.Rounded.Add, contentDescription = "Add Note")
            }
        }

        if (showDialog) {
            ShowNoteDialog(note = selectedNote, onDismiss = { showDialog = false }, addNoteAction = { action ->
                noteViewModel.handleNoteAction(action)
                showDialog = false
            })
        }
    }
}

@Composable
fun ShowNoteDialog(
    note: Note?, onDismiss: () -> Unit, addNoteAction: (NoteAction) -> Unit
) {
    DialogWindow(onCloseRequest = onDismiss) {
        EditNote(note = note, addNoteAction = addNoteAction)
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
    val titleStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
    val contentStyle = MaterialTheme.typography.bodyLarge
    val smallTextStyle = MaterialTheme.typography.bodySmall
    Card(modifier = modifier.fillMaxSize(fraction = 0.75f), onClick = { selectNote(note) }) {
        Text(text = "Title: ${note.title}", style = titleStyle)
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Content: ${note.content}", style = contentStyle)
        Text(text = "Created At: ${note.createdAt}", style = smallTextStyle)
        Text(text = "Note Type: ${note.noteType}")
    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
fun EditNote(note: Note?, addNoteAction: (NoteAction) -> Unit, modifier: Modifier = Modifier) {
    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }

    val saveNote: () -> Unit = {
        val newNote = note?.let {
            Note(id = it.id, title = title, content = content, createdAt = it.createdAt, noteType = it.noteType)
        } ?: Note(title = title, content = content)

        addNoteAction(NoteAction.AddNote(newNote))
        title = ""
        content = ""
    }

    val deleteNote: () -> Unit = {
        note?.let {
            addNoteAction(NoteAction.DeleteNote(it.id))
        }
    }

    val titleStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
    val contentStyle = MaterialTheme.typography.bodyLarge

    Column(modifier = modifier) {
        TextInputBox(onValueChange = { title = it }, value = title, textStyle = titleStyle)
        TextInputBox(onValueChange = { content = it }, value = content, textStyle = contentStyle)
        Row {

            ElevatedButton(onClick = {
                if (title.isNotBlank() && content.isNotBlank()) {
                    deleteNote()
                }
            }) { Text(text = "Delete Note") }
            ElevatedButton(onClick = {
                if (title.isNotBlank() && content.isNotBlank()) {
                    saveNote()
                }
            }) { Text(text = "Save Note") }
        }
    }
}

@Composable
fun TextInputBox(
    value: String, onValueChange: (String) -> Unit, textStyle: TextStyle
) {
    TextField(
        singleLine = true,
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        textStyle = textStyle,
    )
}

