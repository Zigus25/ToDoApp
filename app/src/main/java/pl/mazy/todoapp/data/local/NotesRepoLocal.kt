package pl.mazy.todoapp.data.local

import pl.mazy.todoapp.Database
import pl.mazy.todoapp.data.interfaces.NotesInter
import pl.mazy.todoapp.data.model.Note

class NotesRepoLocal(
    private var database: Database
): NotesInter {
    override suspend fun getNotes(): List<Note> {
        val list = mutableListOf<Note>()
        database.noteQueries.selectNotes().executeAsList().forEach {
            list.add(
                Note(
                    id = it.id.toInt(),
                    name = it.name,
                    description = it.description,
                    owner_id = -1
                )
            )
        }
        return list.toList()
    }

    override suspend fun addNote(note: Note) {
        note.description?.let { database.noteQueries.insert(note.name,-1, it) }
    }

    override suspend fun deleteNote(id: Int) {
        database.noteQueries.deleteNote(id.toLong())
    }

    override suspend fun updateNote(note: Note) {
        note.id?.let { note.description?.let { it1 ->
            database.noteQueries.updateNote(note.name,
                it1, it.toLong())
        } }
    }
}