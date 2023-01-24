package pl.mazy.todoapp.logic.data

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.mazy.todoapp.Database

class ToDoRepository(
    private var database: Database
) {
    fun addCategory(taskListName: String) =
        database.calendarQueries.insertCategory(taskListName)

    private fun consolidate(sub:List<Event>, main:List<Event>): List<Event> = if (sub.isEmpty()){ main }else {
        val subM = mutableListOf<Event>()
        val mainM = main.toMutableList()
        sub.reversed().forEach{
            val a = main.indexOfFirst { m -> m.id == it.MainTaskID }
            if (a == -1){
                val su = sub.find { su-> su.id == it.MainTaskID }
                if (su != null) {
                    subM.add(su.copy(SubList = su.SubList + it))

                }
            }else{
                val s = mainM.removeAt(a)
                mainM.add(a,s.copy(SubList = s.SubList+it))
            }
        }
        consolidate(subM,mainM)
    }

    fun getToDos(listName: String): Flow<List<Event>> =
        database.calendarQueries.selecFromtList(listName).asFlow().mapToList().map {
            val mainList = mutableListOf<Event>()
            val subList = mutableListOf<Event>()
            it.forEach { e ->
                val ev = Event(e.id,e.Name,e.Description,e.Category,e.TimeStart,e.TimeEnd,e.DateStart,e.DateEnd,e.Type,e.Checked,e.Color,e.MainTaskID, listOf())
                if (e.MainTaskID==null){mainList.add(ev)}else{subList.add(ev)} }
            return@map consolidate(subList,mainList)
        }

    fun getTusk(): List<String> =
        database.calendarQueries.selectCategorys().executeAsList()

    fun changeCheck(event: Event) {
        database.calendarQueries.toglleState(event.id)
        if (event.MainTaskID==null&&event.Checked) {
            database.calendarQueries.changeStateTrueWhere(event.id)
        }else if (event.MainTaskID!=null&&database.calendarQueries.countSubTaskFalse(event.MainTaskID).executeAsOne().toInt() >0){
            database.calendarQueries.changeStateFalse(event.MainTaskID)

        }
    }

    fun deleteGroup(name: String) {
        database.calendarQueries.deleteCategory(name, name)
    }
}
