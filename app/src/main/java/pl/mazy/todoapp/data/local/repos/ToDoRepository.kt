//package pl.mazy.todoapp.data.local.repos
//
//import com.squareup.sqldelight.runtime.coroutines.asFlow
//import com.squareup.sqldelight.runtime.coroutines.mapToList
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.map
//import pl.mazy.todoapp.Category
//import pl.mazy.todoapp.Database
//import pl.mazy.todoapp.data.model.Event
//
//class ToDoRepository(
//    private var database: Database
//) {
//    fun addCategory(taskListName: String,owner: Long?) =
//        database.calendarQueries.insertCategory(taskListName,owner)
//
//    private fun consolidate(events:List<Event>): List<Event> {
//        val list = events.toMutableList()
//        for (i in (list.size -1) downTo 0) {
//            val e = list[i]
//            if (e.mainTask_id != null) {
//                val j = list.indexOfFirst { it.id == e.mainTask_id }
//                list[j] = list[j].copy(subList = list[j].subList + e)
//            }
//        }
//        return list.filter { it.mainTask_id == null }
//    }
//    fun getToDos(listName: Long): Flow<List<Event>> =
//        database.calendarQueries.selecFromtList(listName).asFlow().mapToList().map {
//            it.map { e ->
//                Event(
//                    e.id,
//                    e.Name,
//                    e.Description,
//                    e.Category,
//                    e.TimeStart,
//                    e.TimeEnd,
//                    e.DateStart,
//                    e.DateEnd,
//                    e.Type,
//                    e.Checked,
//                    e.Color,
//                    e.MainTaskID,
//                    listOf()
//                )
//            }
//        }.map(::consolidate)
//
//    fun getTusk(owner:Long?): List<Category> =
//        database.calendarQueries.selectCategorys(owner).executeAsList()
//
//    fun changeCheck(event: Event) {
//        database.calendarQueries.toggleState(event.id)
//        if (event.subList.isNotEmpty()){
//            toggleCheckSub(event)
//        }
//        toggleCheckBack(event)
//    }
//
//    private fun toggleCheckBack(ev: Event){
//        if (ev.mainTask_id!=null) {
//            database.calendarQueries.changeStateFalse(ev.mainTask_id)
//            val c = database.calendarQueries.selById(ev.mainTask_id).executeAsOne()
//            if (c.MainTaskID!=null){
//                toggleCheckBack(Event(c.id,c.Name,c.Description,c.Category,c.TimeStart,c.TimeEnd,c.DateStart,c.DateEnd,c.Type,c.Checked,c.Color,c.MainTaskID, listOf()))
//            }
//        }
//    }
//
//    private fun toggleCheckSub(ev: Event){
//        database.calendarQueries.changeStateTrueWhere(ev.id)
//        if (ev.subList.isNotEmpty()){
//            ev.subList.forEach{
//                toggleCheckSub(it)
//            }
//        }
//    }
//
//    fun deleteGroup(id: Long) {
//        database.calendarQueries.deleteCategory(id,id)
//    }
//}
