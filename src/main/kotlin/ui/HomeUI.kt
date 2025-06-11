package ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import vm.HomeViewModel

@Composable
fun HomeUI(homeViewModel: HomeViewModel) {
    Text("Welcome to the Home Screen")
    NoteHomeUI(noteViewModel = AppManager.noteViewModel)
}
