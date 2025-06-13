@file:OptIn(ExperimentalUuidApi::class)

package ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import model.Note
import vm.NoteViewModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.uuid.ExperimentalUuidApi


internal val dateTimeFormatter = DateTimeFormatter.ofPattern("M dd yyyy HH:mm:ss")
    .withZone(ZoneId.systemDefault())

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
                note = it, addNoteAction = { action ->
                    noteViewModel.handleNoteAction(action)
                })
        }

        FloatingActionButton(
            onClick = { selectedNote = Note(title = "", content = "", category = "") }) {
            Icon(Icons.Rounded.Add, contentDescription = "Add Note")
        }
    }
}

