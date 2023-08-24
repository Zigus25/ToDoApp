package pl.mazy.todoapp.data.local

import pl.mazy.todoapp.Database
import pl.mazy.todoapp.data.interfaces.TasksInter
import pl.mazy.todoapp.data.model.Category
import pl.mazy.todoapp.data.model.Event

class TasksRepoLocal(
    private var database: Database
): TasksInter {
    override suspend fun addCategory(name: String) {
        database.calendarQueries.insertCategory(name)
    }

    override suspend fun getCategory(): List<Category> {
        val list = mutableListOf<Category>()
        database.calendarQueries.selectCategorys().executeAsList().forEach{
            list.add(Category(it.id.toInt(),it.name,-1,null))
        }
        return list.toList()
    }

    override suspend fun shareCategory(cId: Int, sId: Int) { //Do nothing
    }

    fun consolidate(events:List<Event>): List<Event> {
        val list = events.toMutableList()
        for (i in (list.size -1) downTo 0) {
            val e = list[i]
            if (e.mainTask_id != null) {
                val j = list.indexOfFirst { it.id == e.mainTask_id }
                list[j] = list[j].copy(subList = list[j].subList + e)
            }
        }
        return list.filter { it.mainTask_id == null }
    }

    override suspend fun delCategory(id: Int) {
        database.calendarQueries.deleteCategory(id.toLong(),id.toLong())
    }

    override suspend fun getTusks(id: Int): List<Event> {
        val list = mutableListOf<Event>()
        database.calendarQueries.selecFromtList(id.toLong()).executeAsList().forEach {
            list.add(Event(
                id = it.id.toInt(),
                owner_id = -1,
                name = it.Name,
                description = it.Description,
                category_id = it.Category.toInt(),
                timeStart = it.TimeStart,
                timeEnd = it.TimeEnd,
                dateStart = it.DateStart,
                dateEnd = it.DateEnd,
                type = it.Type,
                checked = it.Checked,
                color = it.Color,
                mainTask_id = it.MainTaskID?.toInt(),
                subList = listOf()
            ))
        }
        return consolidate(list.toList())
    }

    override suspend fun unmarkAll(ev: Event) {
        database.calendarQueries.changeStateFalse(ev.id!!.toLong())
        if (ev.subList.isNotEmpty()){
            for (e:Event in ev.subList){
                unmarkAll(e)
            }
        }
    }

    override suspend fun toggle(event: Event) {
        database.calendarQueries.toggleState(event.id!!.toLong())
        if (event.subList.isNotEmpty()){
            toggleCheckSub(event)
        }
        toggleCheckBack(event)
    }

    private fun toggleCheckBack(ev: Event){
        if (ev.mainTask_id!=null) {
            database.calendarQueries.changeStateFalse(ev.mainTask_id.toLong())
            val c = database.calendarQueries.selById(ev.mainTask_id.toLong()).executeAsOne()
            if (c.MainTaskID!=null){
                toggleCheckBack(
                    Event(
                        id= c.id.toInt(),
                        owner_id = -1,
                        name = c.Name,
                        description = c.Description,
                        category_id = c.Category.toInt(),
                        timeStart = c.TimeStart,
                        timeEnd = c.TimeEnd,
                        dateStart = c.DateStart,
                        dateEnd = c.DateEnd,
                        type = c.Type,
                        checked = c.Checked,
                        color = c.Color,
                        mainTask_id = c.MainTaskID.toInt(),
                        subList = listOf()))
            }
        }
    }

    private fun toggleCheckSub(ev: Event){
        database.calendarQueries.changeStateTrueWhere(ev.id!!.toLong())
        if (ev.subList.isNotEmpty()){
            ev.subList.forEach{
                toggleCheckSub(it)
            }
        }
    }
}