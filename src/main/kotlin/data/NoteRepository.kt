package data

import model.Note
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class NoteRepository(val noteDAO: NoteDAO) {
    private val notes = mutableListOf<Note>()

    fun addNote(note: Note) {
        notes.add(note)
        noteDAO.upsertNote(NoteDTO.fromNote(note))
    }

    @OptIn(ExperimentalUuidApi::class)
    fun removeNote(noteId: Uuid) {
        notes.removeIf { it.id == noteId }
        noteDAO.deleteNote(noteId)
    }

    fun loadAllNotes(): List<Note> {
        if (notes.isEmpty()) {
            notes.addAll(noteDAO.getAllNotes().map(NoteDTO::toNote))
        }
        return notes.toList()
    }
}