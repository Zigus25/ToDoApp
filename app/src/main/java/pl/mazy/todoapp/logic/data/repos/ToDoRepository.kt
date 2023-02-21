package pl.mazy.todoapp.logic.data.repos

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.mazy.todoapp.Database
import pl.mazy.todoapp.logic.data.Event

class ToDoRepository(
    private var database: Database
) {
    fun addCategory(taskListName: String) =
        database.calendarQueries.insertCategory(taskListName,"")

    private fun consolidate(events:List<Event>): List<Event> {
        val list = events.toMutableList()
        for (i in (list.size -1) downTo 0) {
            val e = list[i]
            if (e.MainTaskID != null) {
                val j = list.indexOfFirst { it.id == e.MainTaskID }
                list[j] = list[j].copy(SubList = list[j].SubList + e)
            }
        }
        return list.filter { it.MainTaskID == null }
    }

    fun getToDos(listName: String): Flow<List<Event>> =
        database.calendarQueries.selecFromtList(listName).asFlow().mapToList().map {
            it.map { e ->
                Event(
                    e.id,
                    e.Name,
                    e.Description,
                    e.Category,
                    e.TimeStart,
                    e.TimeEnd,
                    e.DateStart,
                    e.DateEnd,
                    e.Type,
                    e.Checked,
                    e.Color,
                    e.MainTaskID,
                    listOf()
                )
            }
        }.map(::consolidate)

    fun getTusk(): List<String> =
        database.calendarQueries.selectCategorys().executeAsList()

    fun changeCheck(event: Event) {
        database.calendarQueries.toggleState(event.id)
        if (event.SubList.isNotEmpty()){
            toggleCheckSub(event)
        }
        toggleCheckBack(event)
    }

    private fun toggleCheckBack(ev: Event){
        if (ev.MainTaskID!=null) {
            database.calendarQueries.changeStateFalse(ev.MainTaskID)
            val c = database.calendarQueries.selById(ev.MainTaskID).executeAsOne()
            if (c.MainTaskID!=null){
                toggleCheckBack(Event(c.id,c.Name,c.Description,c.Category,c.TimeStart,c.TimeEnd,c.DateStart,c.DateEnd,c.Type,c.Checked,c.Color,c.MainTaskID, listOf()))
            }
        }
    }
    
    private fun toggleCheckSub(ev: Event){
        database.calendarQueries.changeStateTrueWhere(ev.id)
        if (ev.SubList.isNotEmpty()){
            ev.SubList.forEach{
                toggleCheckSub(it)
            }
        }
    }

    fun deleteGroup(name: String) {
        database.calendarQueries.deleteCategory(name, name)
    }
}
