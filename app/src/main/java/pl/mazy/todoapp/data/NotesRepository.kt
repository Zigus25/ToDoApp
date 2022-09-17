package pl.mazy.todoapp.data

import pl.mazy.todoapp.Database
import pl.mazy.todoapp.Notes

class NotesRepository (
    private var database: Database
) {
    fun getNotes(): List<Notes> =
        database.taskQueries.selectNotes().executeAsList().map {
            it
        }

    fun addNote(name:String,description:String) =
        database.taskQueries.insert(name,description,false)

    fun updateArchive(name: String) =
        database.taskQueries.updateArchive(name)

    fun deleteNote(name: String,description: String) =
        database.taskQueries.deleteNote(name, description)

    fun updateNote(name: String,description: String, oldName:String) =
        database.taskQueries.updateNote(name,description,oldName)

}