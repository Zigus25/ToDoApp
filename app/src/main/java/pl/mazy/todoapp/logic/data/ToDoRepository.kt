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

    fun updateState(event: Event) =
        database.calendarQueries.updateState(
            event.Name,
            event.Categoty,
            event.Description,
            event.DateStart,
            event.Color
        )

    fun deleteGroup(name: String) {
        database.calendarQueries.deleteCategory(name, name)
    }
}
