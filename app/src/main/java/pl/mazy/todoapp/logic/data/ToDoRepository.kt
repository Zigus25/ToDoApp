package pl.mazy.todoapp.logic.data

import pl.mazy.todoapp.Database
import pl.mazy.todoapp.Event

class ToDoRepository(
    private var database: Database
) {
    fun addCategory(taskListName: String) =
        database.calendarQueries.insertCategory(taskListName)

    fun getToDos(listName: String): List<Event> =
        database.calendarQueries.selecFromtList(listName).executeAsList()

    fun getTusk(): List<String> =
        database.calendarQueries.selectCategorys().executeAsList()

    fun updateState(event: Event) {
        database.calendarQueries.updateState(event.id)
        if (event.MainTaskID==null) {
            if (event.Checked) {
                selSubListByID(event.id).forEach {
                    database.calendarQueries.updateStateTrue(it.id)
                }
            }
        }else{
            if (database.calendarQueries.countSubTaskFalse(event.MainTaskID).executeAsOne().toInt() >0){
                database.calendarQueries.updateStateFalse(event.MainTaskID)
            }
        }
    }

    fun selSubListByID(id:Long) =
        database.calendarQueries.selEventByID(id).executeAsList()

    fun deleteGroup(name: String) {
        database.calendarQueries.deleteCategory(name, name)
    }
}
