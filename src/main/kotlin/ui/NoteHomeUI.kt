package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import model.Note
import vm.NoteViewModel

@Composable
fun NoteHomeUI(noteViewModel: NoteViewModel) {
    val notes = noteViewModel.notes

    Column {
        notes.forEach { note ->
            NoteView(note = note)
        }
    }
    FloatingActionButton(onClick = {}) {
        Icon(MaterialIcon.add, contentDescription = "Add Note")
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
    var value by remember { mutableStateOf("") }
    TextField(
        singleLine = true,
        value = value,
        onValueChange = { value = it },
        label = { Text(text = "Enter note title") })
}