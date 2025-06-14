package model

import androidx.compose.ui.graphics.Color
import kotlin.random.Random
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class Task(
    val id: Uuid = Uuid.random(),
    val title: String,
    val content: String,
    val category: String,
    val status: TaskStatus = TaskStatus.Unchecked,
    val statusUpdateDT: Long = System.currentTimeMillis(),
    val tags: Set<String> = emptySet(),
    val updatedAt: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis(),
    val taskType: TaskType = TaskType.Regular,
    val subtaskIDs: Set<Uuid> = emptySet(),
    val color: Color = Color(
        color = Random.nextInt(0xFF_000000.toInt(), 0xFF_FFFFFF.toInt())
    )
)

enum class TaskType {
    Regular, Subtask, Supertask
}

enum class TaskStatus { Unchecked, Mixed, Checked }