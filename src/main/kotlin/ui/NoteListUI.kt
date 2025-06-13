@file:OptIn(ExperimentalUuidApi::class)

package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import model.Note
import java.time.Instant
import kotlin.uuid.ExperimentalUuidApi


@Composable
fun NoteList(notes: List<Note>, selectNote: (Note) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        items(notes, key = { it.id }) { note ->
            NoteView(note = note, selectNote = selectNote)
        }
    }
}


@Composable
fun NoteView(note: Note, selectNote: (Note) -> Unit, modifier: Modifier = Modifier) {
    val titleStyle = MaterialTheme.typography.titleLarge.copy(
        fontWeight = FontWeight.Bold, shadow = Shadow(color = Color.Black, offset = Offset(1f, 1f), blurRadius = 2f)
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
        Row(
            modifier = Modifier.fillMaxWidth().background(color = note.color.copy(alpha = 0.5f)),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = dateTimeFormatter.format(Instant.ofEpochMilli(note.createdAt)),
                style = smallTextStyle
            )
            Text(text = "Note Type: ${note.noteType}")
        }
    }
}

