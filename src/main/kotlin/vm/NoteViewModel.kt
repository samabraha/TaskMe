@file:OptIn(ExperimentalUuidApi::class)

package vm

import data.NoteRepository
import logger
import model.Note
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


class NoteViewModel(val noteRepository: NoteRepository) {
    val notes = noteRepository.loadAllNotes()

    fun handleNoteAction(action: NoteAction) {
        when (action) {
            is NoteAction.AddNote -> updateNote(action.note)
            is NoteAction.DeleteNote -> {
                deleteNote(action.noteId)
            }

            is NoteAction.GetAllNotes -> getAllNotes()
        }
    }

    fun updateNote(note: Note) {
        logger.info("color.value: ${note.color.value}")
        noteRepository.addNote(note)
    }

    fun deleteNote(noteId: Uuid) {
        noteRepository.removeNote(noteId)
    }

    fun getAllNotes(): List<Note> {
        return noteRepository.loadAllNotes()
    }
}

sealed class NoteAction {
    data class AddNote(val note: Note) : NoteAction()
    data class DeleteNote(val noteId: Uuid) : NoteAction()
    object GetAllNotes : NoteAction()
}