package pl.mazy.todoapp.data.local

import pl.mazy.todoapp.Database
import pl.mazy.todoapp.data.interfaces.NotesInter
import pl.mazy.todoapp.data.model.Note

class NotesRepoLocal(
    private var database: Database
): NotesInter {
    override suspend fun getNotes(): List<Note> {
        TODO("Not yet implemented")
    }

    override suspend fun addNote(note: Note) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNote(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun updateNote(note: Note) {
        TODO("Not yet implemented")
    }
}