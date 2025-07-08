@file:OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)

package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import model.Task
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaInstant
import kotlin.uuid.ExperimentalUuidApi


@Composable
fun TaskList(tasks: List<Task>, selectNote: (Task) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        items(tasks, key = { it.id }) { note ->
            TaskView(task = note, selectNote = selectNote)
        }
    }
}


@Composable
fun TaskView(task: Task, selectNote: (Task) -> Unit, modifier: Modifier = Modifier) {
    val titleStyle = MaterialTheme.typography.titleLarge.copy(
        fontWeight = FontWeight.Bold, shadow = Shadow(color = Color.Black, offset = Offset(1f, 1f), blurRadius = 2f)
    )

    MaterialTheme.typography.bodyLarge
    val smallTextStyle = MaterialTheme.typography.bodySmall

    Card(
        modifier = modifier.padding(horizontal = 6.dp, vertical = 3.dp).fillMaxSize().padding(4.dp),
        onClick = { selectNote(task) }) {
        Text(
            text = task.title,
            style = titleStyle,
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier.background(task.color).fillMaxWidth().padding(5.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth().background(color = task.color.copy(alpha = 0.5f)),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = dateTimeFormatter.format(task.createdAt.toJavaInstant()),
                style = smallTextStyle
            )
            Text(text = "Note Type: ${task.taskType}")
        }
    }
}

