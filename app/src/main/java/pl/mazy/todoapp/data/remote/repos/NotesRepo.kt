package pl.mazy.todoapp.data.remote.repos

import pl.mazy.todoapp.data.LoginData
import pl.mazy.todoapp.data.interfaces.NotesInter
import pl.mazy.todoapp.data.interfaces.TasksInter
import pl.mazy.todoapp.data.model.Note
import pl.mazy.todoapp.data.remote.TDAService

class NotesRepo(val api: TDAService): NotesInter {
    override suspend fun getNotes(): List<Note> {
        return api.getNotes(LoginData.token)
    }

    override suspend fun addNote(note: Note) {
        api.newNote(LoginData.token,note)
    }

    override suspend fun deleteNote(id: Int) {
        api.delNote(LoginData.token,id)
    }

    override suspend fun updateNote(note: Note) {
        api.newNote(LoginData.token,note)
    }
}