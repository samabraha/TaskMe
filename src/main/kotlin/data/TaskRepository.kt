@file:OptIn(ExperimentalUuidApi::class)

package data

import model.Task
import model.TaskType
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class TaskRepository(val taskDAO: TaskDAO) {
    val regularTasks: MutableList<Task>
    val subtasks: List<Task>
    val categories: Set<String>

    init {
        regularTasks = loadRegularTasks()
        subtasks = loadSubtasks()
        categories = buildSet {
            add("All")
            addAll(regularTasks.map { it.category }.filter { it.isNotBlank() })
        }
    }

    fun addNote(task: Task) {
        taskDAO.upsertTask(TaskDTO.fromTask(task))
    }

    fun removeNote(noteId: Uuid) {
        taskDAO.deleteNote(noteId)
    }

    fun loadRegularTasks(): MutableList<Task> {
        return buildList {
            addAll(taskDAO.allTasks.map(TaskDTO::toTask))
        }.toMutableList()
    }

    fun loadSubtasks(): List<Task> {
        return buildList {
            taskDAO.allTasks.filter { it.taskType == TaskType.Subtask.name }.map(TaskDTO::toTask)
        }
    }
}