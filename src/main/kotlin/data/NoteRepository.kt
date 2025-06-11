package data

import model.Note

class NoteRepository {
    private val notes = mutableListOf<Note>()

    fun addNote(note: Note) {
        notes.add(note)
    }

    fun loadAllNotes(): List<Note> {
        return notes.toList()
    }
}