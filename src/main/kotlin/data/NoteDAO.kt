package data

import logger
import java.sql.Connection
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class NoteDAO(val connection: Connection) {
    // Function to add a note to the database
    @OptIn(ExperimentalStdlibApi::class)
    fun upsertNote(note: NoteDTO) {
        logger.info("Adding note with ID: ${note.id} to the database")
        val sql =
            """REPLACE INTO ${NoteTable.TABLE_NAME} (${NoteTable.entries.joinToString { it.columnName }}) 
                VALUES (${NoteTable.entries.joinToString { "?" }})"""
        connection.prepareStatement(sql).use { statement ->
            statement.setString(NoteTable.ID.columnIndex, note.id)
            statement.setString(NoteTable.TITLE.columnIndex, note.title)
            statement.setString(NoteTable.CONTENT.columnIndex, note.content)
            statement.setString(NoteTable.CATEGORY.columnIndex, note.category)
            statement.setString(NoteTable.TAGS.columnIndex, note.tags)
            statement.setLong(NoteTable.CREATED_AT.columnIndex, note.createdAt)
            statement.setLong(NoteTable.UPDATED_AT.columnIndex, note.updatedAt)
            statement.setInt(NoteTable.COLOR.columnIndex, note.color)
            statement.setString(NoteTable.NOTE_TYPE.columnIndex, note.noteType)
            statement.executeUpdate()
        }
    }

    fun getAllNotes(): List<NoteDTO> {
        logger.info("Fetching all notes from the database")
        val sql = "SELECT * FROM ${NoteTable.TABLE_NAME}"
        val notes = mutableListOf<NoteDTO>()
        connection.createStatement().use { statement ->
            val resultSet = statement.executeQuery(sql)
            while (resultSet.next()) {
                val id = resultSet.getString(NoteTable.ID.columnName)
                val title = resultSet.getString(NoteTable.TITLE.columnName)
                val content = resultSet.getString(NoteTable.CONTENT.columnName)
                val category = resultSet.getString(NoteTable.CATEGORY.columnName)
                val tags = resultSet.getString(NoteTable.TAGS.columnName) ?: "[]"
                val createdAt = resultSet.getLong(NoteTable.CREATED_AT.columnName)
                val updatedAt = resultSet.getLong(NoteTable.UPDATED_AT.columnName)
                val color = resultSet.getInt(NoteTable.COLOR.columnName)
                val noteType = resultSet.getString(NoteTable.NOTE_TYPE.columnName)
                notes.add(
                    NoteDTO(
                        id = id,
                        title = title,
                        content = content,
                        category = category,
                        tags = tags,
                        createdAt = createdAt,
                        updatedAt = updatedAt,
                        color = color,
                        noteType = noteType
                    )
                )
            }
        }
        return notes
    }

    fun createTable(dropExisting: Boolean) {
        logger.info("Creating notes table if it does not exist")
        val columnDefs = NoteTable.entries.joinToString { "${it.columnName} ${it.type.text}" }

        deleteExistingTable(dropExisting)

        val sql = """
            CREATE TABLE IF NOT EXISTS ${NoteTable.TABLE_NAME} ( $columnDefs, 
            PRIMARY KEY (${NoteTable.ID.columnName}))
        """
        connection.createStatement().use { statement ->
            statement.execute(sql)
        }
    }

    private fun deleteExistingTable(dropExisting: Boolean) {
        if (dropExisting) {
            connection.createStatement().use {
                logger.info("Dropping '${NoteTable.TABLE_NAME}' from database")
                it.execute("DROP TABLE IF EXISTS ${NoteTable.TABLE_NAME}")
            }
        }
    }

    fun deleteNote(noteId: Uuid) {
        logger.info("Deleting note with ID: $noteId from the database")
        val sql = "DELETE FROM ${NoteTable.TABLE_NAME} WHERE ${NoteTable.ID.columnName} = ?"
        connection.prepareStatement(sql).use { statement ->
            statement.setString(1, noteId.toString())
            statement.executeUpdate()
        }
    }
}

enum class NoteTable(val columnName: String, val type: SqliteType) {
    ID("note_id", SqliteType.NonNullText),
    TITLE("title", SqliteType.NonNullText),
    CONTENT("content", SqliteType.NonNullText),
    CATEGORY("category", SqliteType.NonNullText),
    TAGS("tags", SqliteType.NonNullText),
    CREATED_AT("created_at", SqliteType.Integer),
    UPDATED_AT("updated_at", SqliteType.Integer),
    COLOR("color", SqliteType.Integer),
    NOTE_TYPE("note_type", SqliteType.NonNullText);

    val columnIndex = ordinal + 1

    companion object {
        const val TABLE_NAME = "notes"
    }
}
