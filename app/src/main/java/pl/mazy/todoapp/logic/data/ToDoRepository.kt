package pl.mazy.todoapp.logic.data

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import pl.mazy.todoapp.Database
import pl.mazy.todoapp.Event

class ToDoRepository(
    private var database: Database
) {
    fun addCategory(taskListName: String) =
        database.calendarQueries.insertCategory(taskListName)

    fun getToDos(listName: String): Flow<List<Event>> =
        database.calendarQueries.selecFromtList(listName).asFlow().mapToList()

    fun getTusk(): List<String> =
        database.calendarQueries.selectCategorys().executeAsList()

    fun updateState(event: Event) {
        database.calendarQueries.changeState(event.id)
        if (event.MainTaskID==null&&event.Checked) {
            database.calendarQueries.changeStateTrueWhere(event.id)
        }else if (event.MainTaskID!=null&&database.calendarQueries.countSubTaskFalse(event.MainTaskID).executeAsOne().toInt() >0){
            database.calendarQueries.changeStateFalse(event.MainTaskID)

        }
    }

    fun selSubListByID(id:Long) =
        database.calendarQueries.selEventByID(id).executeAsList()

    fun deleteGroup(name: String) {
        database.calendarQueries.deleteCategory(name, name)
    }
}
