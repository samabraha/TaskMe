package ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import vm.HomeViewModel

@Composable
fun HomeUI(homeViewModel: HomeViewModel, modifier: Modifier = Modifier) {
    Text("Welcome to the Home Screen")
    NoteHomeUI(noteViewModel = AppManager.noteViewModel, modifier = modifier)
}
