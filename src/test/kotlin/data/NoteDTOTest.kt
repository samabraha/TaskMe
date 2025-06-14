@file:OptIn(ExperimentalUuidApi::class)

package data

import model.Task
import org.junit.jupiter.api.Test
import kotlin.uuid.ExperimentalUuidApi


class NoteDTOTest {

    @Test
    fun `restore the correct color for black and white`() {
        val taskDTO = TaskDTO.fromTask(Task(title = "", content = "", category = ""))
    }

}