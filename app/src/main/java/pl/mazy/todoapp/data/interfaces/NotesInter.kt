package pl.mazy.todoapp.data.interfaces

import pl.mazy.todoapp.data.model.Note

interface NotesInter {
    suspend fun getNotes(): List<Note>

    suspend fun addNote(note: Note)

    suspend fun deleteNote(id:Int)

    suspend fun updateNote(note: Note)
}