package pl.mazy.todoapp.data.local

import kotlinx.coroutines.flow.Flow
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

    override suspend fun toggle(id: Int) {
        database.calendarQueries.toggleState(id.toLong())
    }
}