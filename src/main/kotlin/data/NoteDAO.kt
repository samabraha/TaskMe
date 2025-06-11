package data

import model.Note
import model.NoteType
import java.sql.Connection

class NoteDAO(val connection: Connection) {
    // Function to add a note to the database
    fun addNote(note: Note) {
        val sql = "INSERT INTO notes (title, content, created_at, note_type) VALUES (?, ?, ?, ?)"
        connection.prepareStatement(sql).use { statement ->
            statement.setString(1, note.title)
            statement.setString(2, note.content)
            statement.setLong(3, note.createdAt)
            statement.setString(4, note.noteType.name)
            statement.executeUpdate()
        }
    }

    // Function to get all notes from the database
    fun getAllNotes(): List<Note> {
        val sql = "SELECT title, content, created_at, note_type FROM notes"
        val notes = mutableListOf<Note>()
        connection.createStatement().use { statement ->
            val resultSet = statement.executeQuery(sql)
            while (resultSet.next()) {
                val title = resultSet.getString("title")
                val content = resultSet.getString("content")
                val createdAt = resultSet.getLong("created_at")
                val noteType = NoteType.valueOf(resultSet.getString("note_type"))
                notes.add(Note(title, content, createdAt, noteType))
            }
        }
        return notes
    }
}