package pl.mazy.todoapp.logic.data.repos

import android.annotation.SuppressLint
import pl.mazy.todoapp.Database
import pl.mazy.todoapp.logic.data.Event
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class CalendarRepository (
    private var database: Database
) {
    private val format: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val today: String = LocalDate.now().format( format)

    @SuppressLint("WeekBasedYear")
    fun selEvents():List<Event>{
        return database.calendarQueries.selBetweenDate(today,today).executeAsList().map {
                e -> Event(e.id,e.Name,e.Description,e.Category,e.TimeStart,e.TimeEnd,e.DateStart,e.DateEnd,e.Type,e.Checked,e.Color,e.MainTaskID, listOf())
        }
    }

    fun selMaxDate(): String? {
        return database.calendarQueries.selMaxDateEnd(today,today).executeAsOne().MAX
    }

    fun deleteEvent(ev: Event){
        database.calendarQueries.deleteEvent(ev.id,ev.id)
        if (ev.SubList.isNotEmpty()){
            ev.SubList.forEach {
                deleteEvent(it)
            }
        }
    }

    fun addEvent(ev: Event, subList:List<String>,owner:Long){
        database.calendarQueries.addEvent(owner,ev.Name,ev.Description,ev.Category,ev.TimeStart,ev.TimeEnd,ev.DateStart,ev.DateEnd,ev.Type,ev.Checked,ev.Color,ev.MainTaskID)
        val id = database.calendarQueries.selMyID().executeAsOne().max
        subList.forEach{
            database.calendarQueries.addEvent(owner,it,"",ev.Category,null,null,null,null, Type = true, Checked = false, ev.Color,id)

        }
    }

    fun updateEvent(event: Event, subList: List<String>){
        database.calendarQueries.updateEvent(event.Name,event.Description,event.Category,event.TimeStart,event.TimeEnd,event.DateStart,event.DateEnd,event.Type,event.Checked,event.Color,event.MainTaskID,event.id)
        val size:Int = selectSubList(event.id).size
        for (index in size until subList.size){
            database.calendarQueries.addEvent(
                owner = null,
                Name =  subList[index],
                Description = "",
                Category = event.Category,
                TimeStart = null,
                TimeEnd = null,
                DateStart = null,
                DateEnd = null,
                Type = true,
                Checked = false,
                Color = event.Color,
                MainTaskID = event.id
            )
        }
        database.calendarQueries.changeStateFalse(event.id)
        toggleCheck(event)
    }
    private fun toggleCheck(ev: Event){
        if (ev.MainTaskID!=null) {
            database.calendarQueries.changeStateFalse(ev.MainTaskID)
            val c = database.calendarQueries.selById(ev.MainTaskID).executeAsOne()
            if (c.MainTaskID!=null){
                toggleCheck(Event(c.id,c.Name,c.Description,c.Category,c.TimeStart,c.TimeEnd,c.DateStart,c.DateEnd,c.Type,c.Checked,c.Color,c.MainTaskID, listOf()))
            }
        }
    }
    fun selectSubList(id:Long):List<String>{
        return database.calendarQueries.selNameByID(id).executeAsList()
    }
}