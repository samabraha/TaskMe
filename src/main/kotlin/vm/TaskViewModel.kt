@file:OptIn(ExperimentalUuidApi::class)

package vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import data.TaskRepository
import model.Task
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


class TaskViewModel(val taskRepository: TaskRepository) {
    var currentTasks: List<Task> by mutableStateOf(getAllTasks())
    var categories = taskRepository.categories.toList()

    fun handleTaskAction(action: TaskAction) {
        when (action) {
            is TaskAction.AddTask -> updateTask(action.task)
            is TaskAction.DeleteTask -> {
                deleteTask(action.noteId)
            }

            is TaskAction.FilterTasks -> {
                currentTasks = if (action.category == "All") {
                    getAllTasks()
                } else {
                    taskRepository.regularTasks.filter { it.category == action.category }
                }
            }
        }
    }

    fun updateTask(task: Task) {
        taskRepository.addNote(task)
    }

    fun deleteTask(noteId: Uuid) {
        taskRepository.removeNote(noteId)
    }

    fun getAllTasks(): List<Task> {
        return taskRepository.loadRegularTasks().toList()
    }
}

sealed class TaskAction {
    data class AddTask(val task: Task) : TaskAction()
    data class DeleteTask(val noteId: Uuid) : TaskAction()
    data class FilterTasks(val category: String) : TaskAction()
}