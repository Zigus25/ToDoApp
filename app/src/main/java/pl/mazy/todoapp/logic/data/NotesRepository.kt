package pl.mazy.todoapp.logic.data

import pl.mazy.todoapp.Database
import pl.mazy.todoapp.Notes

class NotesRepository (
    private var database: Database
) {
    fun getNotes(): List<Notes> =
        database.noteQueries.selectNotes().executeAsList()

    fun addNote(note:Notes) =
        database.noteQueries.insert(note.name,note.description,note.archivied)

    fun updateArchive(id: Long) =
        database.noteQueries.updateArchive(id)

    fun deleteNote(id:Long) =
        database.noteQueries.deleteNote(id)

    fun updateNote(note:Notes) =
        database.noteQueries.updateNote(note.name,note.description,note.id)

}