@file:OptIn(ExperimentalUuidApi::class)

package data

import model.Note
import org.junit.jupiter.api.Test
import kotlin.uuid.ExperimentalUuidApi


class NoteDTOTest {

    @Test
    fun `restore the correct color for black and white`() {
        val noteDTO = NoteDTO.fromNote(Note(title = "", content = "", category = ""))
    }

}