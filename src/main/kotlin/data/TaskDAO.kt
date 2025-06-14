package data

import logger
import java.sql.Connection
import java.sql.PreparedStatement
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class TaskDAO(val connection: Connection) {
    val allTasks: List<TaskDTO>

    init {
        initializeTable()
        allTasks = loadTasksFromDB()
    }

    // Function to add a note to the database
    fun upsertTask(note: TaskDTO) {
        logger.info("Adding note with ID: ${note.id} to the database")
        val sql =
            """REPLACE INTO ${NoteTable.TABLE_NAME} (${NoteTable.entries.joinToString { it.columnName }}) 
                VALUES (${NoteTable.entries.joinToString { "?" }})"""
        connection.prepareStatement(sql).use { statement ->
            statement.setString(NoteTable.ID.columnIndex, note.id)
            statement.setString(NoteTable.TITLE.columnIndex, note.title)
            statement.setString(NoteTable.CONTENT.columnIndex, note.content)
            statement.setString(NoteTable.CATEGORY.columnIndex, note.category)
            statement.setString(NoteTable.STATUS.columnIndex, note.status)
            statement.setLong(NoteTable.STATUS_UPDATE_DT.columnIndex, note.statusUpdateDT)
            statement.setString(NoteTable.TAGS.columnIndex, note.tags)
            statement.setLong(NoteTable.CREATED_DT.columnIndex, note.createdAt)
            statement.setLong(NoteTable.UPDATED_DT.columnIndex, note.updatedAt)
            statement.setString(NoteTable.TASK_TYPE.columnIndex, note.taskType)
            statement.setString(NoteTable.SUB_TASK_IDS.columnIndex, note.subtaskIDs)
            statement.setInt(NoteTable.COLOR.columnIndex, note.color)
            statement.executeUpdate()
        }
    }

    private fun loadTasksFromDB(): List<TaskDTO> {
        logger.info("Fetching all notes from the database")
        val sql = "SELECT * FROM ${NoteTable.TABLE_NAME}"
        return buildList {

            connection.createStatement().use { statement ->
                val resultSet = statement.executeQuery(sql)
                while (resultSet.next()) {
                    val id = resultSet.getString(NoteTable.ID.columnName)
                    val title = resultSet.getString(NoteTable.TITLE.columnName)
                    val content = resultSet.getString(NoteTable.CONTENT.columnName)
                    val category = resultSet.getString(NoteTable.CATEGORY.columnName)
                    val status = resultSet.getString(NoteTable.STATUS.columnName)
                    val statusUpdateDT = resultSet.getLong(NoteTable.STATUS_UPDATE_DT.columnName)
                    val tags = resultSet.getString(NoteTable.TAGS.columnName) ?: "[]"
                    val createdAt = resultSet.getLong(NoteTable.CREATED_DT.columnName)
                    val updatedAt = resultSet.getLong(NoteTable.UPDATED_DT.columnName)
                    val color = resultSet.getInt(NoteTable.COLOR.columnName)
                    val noteType = resultSet.getString(NoteTable.TASK_TYPE.columnName)
                    val subtaskIDs = resultSet.getString(NoteTable.SUB_TASK_IDS.columnName)
                    add(
                        TaskDTO(
                            id = id,
                            title = title,
                            content = content,
                            category = category,
                            status = status,
                            statusUpdateDT = statusUpdateDT,
                            tags = tags,
                            createdAt = createdAt,
                            updatedAt = updatedAt,
                            color = color,
                            taskType = noteType,
                            subtaskIDs = subtaskIDs
                        )
                    )
                }
            }
        }
    }

    fun initializeTable(dropExisting: Boolean = false) {
        logger.info("initializing table '${NoteTable.TABLE_NAME}'")
        deleteExistingTable(dropExisting)
        createNewTable()
    }

    private fun createNewTable() {
        val columns = NoteTable.entries.joinToString { "${it.columnName} ${it.type.text}" }
        val sql = """
            CREATE TABLE IF NOT EXISTS ${NoteTable.TABLE_NAME} ( $columns, 
            PRIMARY KEY (${NoteTable.ID.columnName}))
        """
        connection.createStatement().use { statement ->
            statement.execute(sql)
        }
    }

    private fun deleteExistingTable(dropExisting: Boolean) {
        if (dropExisting) {
            connection.createStatement().use {
                logger.info("Dropping '${NoteTable.TABLE_NAME}' from database")
                it.execute("DROP TABLE IF EXISTS ${NoteTable.TABLE_NAME}")
            }
        }
    }

    fun deleteNote(noteId: Uuid) {
        logger.info("Deleting note with ID: $noteId from the database")
        val sql = "DELETE FROM ${NoteTable.TABLE_NAME} WHERE ${NoteTable.ID.columnName} = ?"
        connection.prepareStatement(sql).use { statement ->
            statement.setString(1, noteId.toString())
            statement.executeUpdate()
        }
    }
}


// Do NOT reorder entries! Used ordinal+1 for SQL column binding.
enum class NoteTable(val columnName: String, val type: SqliteType) {
    ID("task_id", SqliteType.NonNullText),
    TITLE("title", SqliteType.NonNullText),
    CONTENT("content", SqliteType.NonNullText),
    CATEGORY("category", SqliteType.NonNullText),
    STATUS("status", SqliteType.NonNullText),
    STATUS_UPDATE_DT("status_update_dt", SqliteType.Integer),
    TAGS("tags", SqliteType.NonNullText),
    CREATED_DT("created_dt", SqliteType.Integer),
    UPDATED_DT("updated_dt", SqliteType.Integer),
    TASK_TYPE("task_type", SqliteType.NonNullText),
    SUB_TASK_IDS("subtask_ids", SqliteType.NonNullText),
    COLOR("color", SqliteType.Integer);

    val columnIndex = ordinal + 1

    companion object {
        const val TABLE_NAME = "notes"
    }
}
