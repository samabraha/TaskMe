import data.TaskDAO
import data.TaskRepository
import vm.HomeViewModel
import vm.TaskViewModel
import java.nio.file.Path
import java.nio.file.Paths
import java.sql.DriverManager


object AppManager {
    val dbFile: Path = Paths.get("notes.db")
    val homeViewModel = HomeViewModel()
    val taskDAO: TaskDAO
    val taskRepository: TaskRepository
    val taskViewModel: TaskViewModel

    init {
        val connStr = "jdbc:sqlite:${dbFile.toAbsolutePath()}"

        val connection = DriverManager.getConnection(connStr)

        taskDAO = TaskDAO(connection)
        taskRepository = TaskRepository(taskDAO)
        taskViewModel = TaskViewModel(taskRepository)
    }
}