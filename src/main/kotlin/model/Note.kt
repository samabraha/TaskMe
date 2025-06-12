package model

import androidx.compose.ui.graphics.Color
import kotlin.random.Random
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class Note(
    val id: Uuid = Uuid.random(),
    val title: String,
    val content: String,
    val category: String,
    val tags: Set<String> = emptySet(),
    val updatedAt: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis(),
    val noteType: NoteType = NoteType.Regular,
    val color: Color = Color(
        color = Random.nextInt(0xFF_000000.toInt(), 0xFF_FFFFFF.toInt())
    )
)

enum class NoteType {
    Regular
}
