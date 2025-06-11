import data.NoteDAO
import data.NoteRepository
import vm.HomeViewModel
import vm.NoteViewModel
import java.nio.file.Path
import java.nio.file.Paths
import java.sql.DriverManager


object AppManager {
    val dbFile: Path = Paths.get("notes.db")
    val homeViewModel = HomeViewModel()
    val noteDAO: NoteDAO
    val noteRepository: NoteRepository
    val noteViewModel: NoteViewModel

    init {
        val connStr = "jdbc:sqlite:${dbFile.toAbsolutePath()}"

        val connection = DriverManager.getConnection(connStr)
        noteDAO = NoteDAO(connection)
        noteRepository = NoteRepository(noteDAO)
        noteDAO.createTable()
        noteViewModel = NoteViewModel(noteRepository)
    }
}