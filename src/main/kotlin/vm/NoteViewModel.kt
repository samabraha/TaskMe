package vm

import data.NoteRepository
import model.Note


class NoteViewModel(val noteRepository: NoteRepository) {
    val notes = noteRepository.loadAllNotes()

    fun handleNoteAction(action: NoteAction) {
        when (action) {
            is NoteAction.AddNote -> addNote(action.title, action.content)
            is NoteAction.GetAllNotes -> getAllNotes()
            // Handle other actions like update and delete here
        }
    }

    // Example function to add a note
    fun addNote(title: String, content: String) {
        val newNote = Note(title, content, System.currentTimeMillis())
        noteRepository.addNote(newNote)
    }

    // Example function to get all notes
    fun getAllNotes(): List<Note> {
        return noteRepository.loadAllNotes()
    }
}

sealed class NoteAction {
    data class AddNote(val title: String, val content: String) : NoteAction()
    object GetAllNotes : NoteAction()
}