import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.*
import ui.HomeUI
import java.util.logging.Logger


val logger: Logger = Logger.getLogger("com.develogica.task_me")

@Composable
@Preview
fun App() {
    val homeViewModel by remember { mutableStateOf(AppManager.homeViewModel) }

    MaterialTheme {
        Column {
            HomeUI(homeViewModel = homeViewModel)
        }
    }
}


fun main() = application {
    val windowState = rememberWindowState(
        placement = WindowPlacement.Floating,
        position = WindowPosition(alignment = Alignment.Center)
    )

    Window(
        state = windowState,
        onCloseRequest = ::exitApplication
    ) {
        App()
    }
}
