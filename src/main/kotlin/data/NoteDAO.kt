package data

import logger
import model.Note
import model.NoteType
import java.sql.Connection
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class NoteDAO(val connection: Connection) {
    // Function to add a note to the database
    fun addNote(note: Note) {
        logger.info("Adding note with ID: ${note.id} to the database")
        val sql =
            """INSERT INTO ${NoteTableColumn.TABLE_NAME} (${NoteTableColumn.entries.joinToString { it.columnName }}) 
                VALUES (${NoteTableColumn.entries.joinToString { "?" }})"""
        connection.prepareStatement(sql).use { statement ->
            logger.info("Prepared SQL statement: $sql")
            statement.setString(NoteTableColumn.ID.columnIndex, note.id.toString())
            statement.setString(NoteTableColumn.TITLE.columnIndex, note.title)
            statement.setString(NoteTableColumn.CONTENT.columnIndex, note.content)
            statement.setLong(NoteTableColumn.CREATED_AT.columnIndex, note.createdAt)
            statement.setString(NoteTableColumn.NOTE_TYPE.columnIndex, note.noteType.name)
            statement.executeUpdate()
        }
    }

    // Function to get all notes from the database
    fun getAllNotes(): List<Note> {
        logger.info("Fetching all notes from the database")
        val sql = "SELECT * FROM ${NoteTableColumn.TABLE_NAME}"
        val notes = mutableListOf<Note>()
        connection.createStatement().use { statement ->
            val resultSet = statement.executeQuery(sql)
            while (resultSet.next()) {
                val id = resultSet.getString(NoteTableColumn.ID.columnName)
                val title = resultSet.getString(NoteTableColumn.TITLE.columnName)
                val content = resultSet.getString(NoteTableColumn.CONTENT.columnName)
                val createdAt = resultSet.getLong(NoteTableColumn.CREATED_AT.columnName)
                val noteType = NoteType.valueOf(resultSet.getString(NoteTableColumn.NOTE_TYPE.columnName))
                notes.add(
                    Note(
                        id = Uuid.parse(id),
                        title = title,
                        content = content,
                        createdAt = createdAt,
                        noteType = noteType
                    )
                )
            }
        }
        return notes
    }

    fun createTable() {
        logger.info("Creating notes table if it does not exist")
        val columnDefs = NoteTableColumn.entries.joinToString { "${it.columnName} ${it.type.text}" }
        val sql = """
            CREATE TABLE IF NOT EXISTS ${NoteTableColumn.TABLE_NAME} ( $columnDefs, 
            PRIMARY KEY (${NoteTableColumn.ID.columnName}))
        """
        logger.info("Executing SQL to create table: $sql")
        connection.createStatement().use { statement ->
            statement.execute(sql)
        }
    }
}

enum class NoteTableColumn(val columnName: String, val type: SqliteType, val columnIndex: Int) {
    ID("note_id", SqliteType.NonNullText, 1),
    TITLE("title", SqliteType.NonNullText, 2),
    CONTENT("content", SqliteType.NonNullText, 3),
    CREATED_AT("created_at", SqliteType.Integer, 4),
    NOTE_TYPE("note_type", SqliteType.NonNullText, 5);

    companion object {
        const val TABLE_NAME = "notes"
    }
}
