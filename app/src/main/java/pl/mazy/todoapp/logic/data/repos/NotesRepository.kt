package pl.mazy.todoapp.logic.data.repos

import pl.mazy.todoapp.Database
import pl.mazy.todoapp.Notes

class NotesRepository (
    private var database: Database
) {
    fun getNotes(owner:Long?): List<Notes> =
        database.noteQueries.selectNotes(owner).executeAsList()

    fun addNote(note:Notes,owner: Long?) =
        database.noteQueries.insert(note.name,owner,note.description)

    fun deleteNote(id:Long) =
        database.noteQueries.deleteNote(id)

    fun updateNote(note:Notes) =
        database.noteQueries.updateNote(note.name,note.description,note.id)

}