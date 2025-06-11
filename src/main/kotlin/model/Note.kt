package model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class Note @OptIn(ExperimentalUuidApi::class) constructor(
    val id: Uuid = Uuid.random(),
    val title: String,
    val content: String,
    val createdAt: Long = System.currentTimeMillis(),
    val noteType: NoteType = NoteType.Regular
) {
}

enum class NoteType {
    Regular
}
