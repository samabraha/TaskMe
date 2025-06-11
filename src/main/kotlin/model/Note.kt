package model

class Note(
    val title: String,
    val content: String,
    val createdAt: Long = System.currentTimeMillis(),
    val noteType: NoteType = NoteType.Regular
) {
}

enum class NoteType {
    Regular
}
