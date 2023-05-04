package pl.mazy.todoapp.data.remote.repos

import pl.mazy.todoapp.data.LoginData
import pl.mazy.todoapp.data.interfaces.NotesInter
import pl.mazy.todoapp.data.interfaces.TasksInter
import pl.mazy.todoapp.data.model.Note
import pl.mazy.todoapp.data.remote.TDAService

class NotesRepo(val api: TDAService): NotesInter {
    val loginData = LoginData
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