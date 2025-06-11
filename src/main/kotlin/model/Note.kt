package model

import androidx.compose.ui.graphics.Color
import kotlin.random.Random
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class Note (
    val id: Uuid = Uuid.random(),
    val title: String,
    val content: String,
    val category: String,
    val createdAt: Long = System.currentTimeMillis(),
    val noteType: NoteType = NoteType.Regular,
    val color: Color = Color(Random.nextLong(0xFF000000, 0xFFFFFFFF))
) {
}

enum class NoteType {
    Regular
}
