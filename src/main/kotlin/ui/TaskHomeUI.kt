@file:OptIn(ExperimentalUuidApi::class)

package ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import model.Task
import vm.TaskAction
import vm.TaskViewModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.uuid.ExperimentalUuidApi


internal val dateTimeFormatter = DateTimeFormatter.ofPattern("M dd yyyy HH:mm:ss")
    .withZone(ZoneId.systemDefault())

@Composable
fun TaskHomeUI(taskViewModel: TaskViewModel, modifier: Modifier = Modifier) {
    val notes = taskViewModel.currentTasks

    var selectedTask by remember { mutableStateOf<Task?>(null) }

    Row(modifier = modifier.fillMaxSize()) {
        CategoryList(
            taskViewModel.categories,
            filterAction = { taskViewModel.handleTaskAction(TaskAction.FilterTasks(it))}
        )
        TaskList(
            tasks = notes,
            selectNote = { note -> selectedTask = if (selectedTask == note) null else note },
            modifier = Modifier.fillMaxWidth(0.5f)
        )

        selectedTask?.let {
            EditableNote(
                task = it, addNoteAction = { action ->
                    taskViewModel.handleTaskAction(action)
                })
        }

        FloatingActionButton(
            onClick = { selectedTask = Task(title = "", content = "", category = "") }) {
            Icon(Icons.Rounded.Add, contentDescription = "Add Note")
        }
    }
}

@Composable
fun CategoryList(categories: List<String>, filterAction: (String) -> Unit) {
    LazyColumn {
        items(categories) { category ->
            ElevatedButton(onClick = { filterAction(category) }) {
                Text(text = category)
            }
        }
    }
}


