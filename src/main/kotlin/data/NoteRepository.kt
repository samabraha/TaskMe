package data

import model.Note

class NoteRepository( val noteDAO: NoteDAO) {
    private val notes = mutableListOf<Note>()

    fun addNote(note: Note) {
        notes.add(note)
        noteDAO.addNote(note)
    }

    fun loadAllNotes(): List<Note> {
        if (notes.isEmpty()) {
            notes.addAll(noteDAO.getAllNotes())
        }
        return notes.toList()
    }
}