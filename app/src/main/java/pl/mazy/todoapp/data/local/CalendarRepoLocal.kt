package pl.mazy.todoapp.data.local

import pl.mazy.todoapp.Database
import pl.mazy.todoapp.data.interfaces.CalendarInter
import pl.mazy.todoapp.data.model.Event
import java.time.LocalDate
import java.util.Date

class CalendarRepoLocal(
    private var database: Database
):CalendarInter {

    val tR = TasksRepoLocal(database)
    override suspend fun addEvent(ev: Event, subList: List<String>) {
            database.calendarQueries.addEvent(
                null,
                ev.name,
                ev.description,
                ev.category_id.toLong(),
                ev.timeStart,
                ev.timeEnd,
                ev.dateStart,
                ev.dateEnd,
                ev.type,
                ev.checked,
                ev.color,
                ev.mainTask_id?.toLong()
            )

        val id = database.calendarQueries.selMyID().executeAsOne().max
        subList.forEach{
            database.calendarQueries.addEvent(null,it,"",ev.category_id.toLong(),null,null,null,null, Type = true, Checked = false, ev.color,id)

        }
    }

    override suspend fun updateEvent(ev: Event, subList: List<String>) {
        ev.id?.let {
            database.calendarQueries.updateEvent(
                ev.name,
                ev.description,
                ev.category_id.toLong(),
                ev.timeStart,
                ev.timeEnd,
                ev.dateStart,
                ev.dateEnd,
                ev.type,
                ev.checked,
                ev.color,
                ev.mainTask_id?.toLong(),
                it.toLong())
        }
        val size: Int? = ev.id?.let { namesSubList(it).size }
        if (size!=null){
            for (index in size until subList.size) {
                database.calendarQueries.addEvent(
                    owner = null,
                    Name = subList[index],
                    Description = "",
                    Category = ev.category_id.toLong(),
                    TimeStart = null,
                    TimeEnd = null,
                    DateStart = null,
                    DateEnd = null,
                    Type = true,
                    Checked = false,
                    Color = ev.color,
                    MainTaskID = ev.id.toLong()
                )
            }
        }
        ev.id?.let { database.calendarQueries.changeStateFalse(it.toLong()) }
//        toggleCheck(event)
    }

    override suspend fun delEvent(ev: Event) {
        ev.id?.let { database.calendarQueries.deleteEvent(it.toLong(),it.toLong()) }
        if (ev.subList.isNotEmpty()){
            ev.subList.forEach {
                delEvent(it)
            }
        }
    }

    override suspend fun namesSubList(id: Int): List<String> {
        return database.calendarQueries.selNameByID(id.toLong()).executeAsList()
    }

    override suspend fun selByDate(date: LocalDate): List<Event> {
        val list = mutableListOf<Event>()
        database.calendarQueries.selBetweenDate(date.toString(),date.toString()).executeAsList().forEach {
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
        return tR.consolidate(list.toList())
    }
}