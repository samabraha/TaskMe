package vm

import data.NoteRepository

class HomeViewModel {
    val noteViewModel: NoteViewModel = NoteViewModel(NoteRepository())
}