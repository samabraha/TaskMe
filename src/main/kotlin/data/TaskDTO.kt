@file:OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)

package data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.serialization.json.Json
import logger
import model.Task
import model.TaskStatus
import model.TaskType
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


data class TaskDTO(
    val id: String,
    val title: String,
    val content: String,
    val category: String,
    val status: String,
    val statusUpdateDT: Long,
    val tags: String,
    val taskType: String,
    val subtaskIDs: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val color: Int
) {
    fun toTask(): Task {
        val colorVal = Color(color)
        return Task(
            id = Uuid.parse(id),
            title = title,
            content = content,
            category = category,
            status = TaskStatus.valueOf(status),
            statusUpdateDT = statusUpdateDT,
            taskType = TaskType.valueOf(taskType),
            subtaskIDs = Json.decodeFromString<Set<Uuid>>(subtaskIDs),
            tags = Json.decodeFromString(tags),
            createdAt = createdAt,
            updatedAt = updatedAt,
            color = colorVal
        )
    }

    companion object {
        fun fromTask(task: Task): TaskDTO {
            val colorArgb = task.color.toArgb()
            logger.info("note.color: ${task.color} toArgb${task.color.toArgb()}")
            return TaskDTO(
                id = task.id.toString(),
                title = task.title,
                content = task.content,
                category = task.category,
                status = task.status.name,
                statusUpdateDT = task.statusUpdateDT,
                tags = Json.encodeToString(task.tags),
                taskType = task.taskType.name,
                subtaskIDs = Json.encodeToString(task.subtaskIDs),
                color = colorArgb,
                createdAt = task.createdAt,
                updatedAt = task.updatedAt,
            )
        }
    }
}
