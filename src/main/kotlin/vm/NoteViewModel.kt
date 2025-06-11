package vm

import data.NoteRepository
import model.Note
import kotlin.uuid.ExperimentalUuidApi


class NoteViewModel(val noteRepository: NoteRepository) {
    val notes = noteRepository.loadAllNotes()

    fun handleNoteAction(action: NoteAction) {
        when (action) {
            is NoteAction.AddNote -> addNote(action.title, action.content)
            is NoteAction.GetAllNotes -> getAllNotes()
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun addNote(title: String, content: String) {
        val newNote = Note(title = title, content = content)
        noteRepository.addNote(newNote)
    }

    fun getAllNotes(): List<Note> {
        return noteRepository.loadAllNotes()
    }
}

sealed class NoteAction {
    data class AddNote(val title: String, val content: String) : NoteAction()
    object GetAllNotes : NoteAction()
}