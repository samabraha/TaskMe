package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.window.DialogWindow
import model.Note
import vm.NoteViewModel

@Composable
fun NoteHomeUI(noteViewModel: NoteViewModel) {
    val notes = noteViewModel.notes
    var showDialog by remember { mutableStateOf(false) }

    Column {
        notes.forEach { note ->
            NoteView(note = note)
        }
    }
    FloatingActionButton(onClick = { showDialog = true }) {
        Icon(Icons.Rounded.Add, contentDescription = "Add Note")
    }
    DialogWindow(
        visible = showDialog, onCloseRequest = { showDialog = false },
    ) {
        Column {
            NewNote()
            FloatingActionButton(onClick = { showDialog = false }) {
                Icon(Icons.Rounded.Add, contentDescription = "Save Note")
            }
        }
    }
}

@Composable
fun NoteView(note: Note) {
    Column {
        Text(text = "Title: ${note.title}")
        Text(text = "Content: ${note.content}")
        Text(text = "Created At: ${note.createdAt}")
        Text(text = "Note Type: ${note.noteType}")
    }
}

@Composable
fun NewNote() {
    Column {
        TextInputBox(label = "Title")
        TextInputBox(label = "Content")
        TextInputBox(label = "Note Type")
    }
}

@Composable
fun TextInputBox(
    label: String,
) {
    var value by remember { mutableStateOf("") }
    TextField(
        singleLine = true,
        value = value,
        onValueChange = { value = it },
        label = { Text(text = label) },
    )
}