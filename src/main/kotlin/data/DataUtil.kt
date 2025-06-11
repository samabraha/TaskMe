package data

enum class SqliteType(val text: String) {
    Text("TEXT"),
    NonNullText("TEXT NOT NULL"),
    NonNullInteger("INTEGER NOT NULL"),
}