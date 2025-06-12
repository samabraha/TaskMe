@file:OptIn(ExperimentalUuidApi::class)

package data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.serialization.json.Json
import logger
import model.Note
import model.NoteType
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


data class NoteDTO(
    val id: String,
    val title: String,
    val content: String,
    val category: String,
    val tags: String,
    val noteType: String,
    val createdAt: Long,
    val updatedAt: Long,
    val color: Int
) {
    fun toNote(): Note {
        val colorVal = Color(color)
        logger.info("color: $color colorObj:${colorVal}")
        return Note(
            id = Uuid.parse(id),
            title = title,
            content = content,
            category = category,
            noteType = NoteType.valueOf(noteType),
            tags = Json.decodeFromString(tags),
            createdAt = createdAt,
            updatedAt = updatedAt,
            color = colorVal
        )
    }

    companion object {
        fun fromNote(note: Note): NoteDTO {
            val colorArgb = note.color.toArgb()
            logger.info("note.color: ${note.color} toArgb${note.color.toArgb()}")
            return NoteDTO(
                id = note.id.toString(),
                title = note.title,
                content = note.content,
                category = note.category,
                tags = Json.encodeToString(note.tags),
                noteType = note.noteType.name,
                color = colorArgb,
                createdAt = note.createdAt,
                updatedAt = note.updatedAt,
            )
        }
    }
}
