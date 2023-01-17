package pl.mazy.todoapp.logic.data

import android.annotation.SuppressLint
import pl.mazy.todoapp.Database
import pl.mazy.todoapp.Event
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class CalendarRepository (
    private var database: Database
) {
    private val format: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val today: String = LocalDate.now().format( format)
    @SuppressLint("WeekBasedYear")
    fun selEvents():List<Event>{
        return database.calendarQueries.selBetweenDate(today,today).executeAsList()
    }

    fun selMaxDate(): String? {
        return database.calendarQueries.selMaxDateEnd(today,today).executeAsOne().MAX
    }

    fun deleteEvent(ev: Event){
        database.calendarQueries.deleteEvent(ev.Name,ev.Categoty,ev.Description,ev.DateStart,ev.Color)
    }

    fun addEvent(ev: Event,subList:List<String>){
        database.calendarQueries.addEvent(ev.Name,ev.Description,ev.Categoty,ev.TimeStart,ev.TimeEnd,ev.DateStart,ev.DateEnd,ev.Type,ev.Checked,ev.Color,ev.MainTaskID)
//        val id = database.calendarQueries.selMyID(ev.Name,ev.Categoty,ev.Description,ev.DateStart,ev.Color).executeAsOne()
        subList.forEach{
            database.calendarQueries.addEvent(it,"",ev.Categoty,null,null,null,null, Type = true, Checked = false, ev.Color,null)

        }
    }

    fun updateEvent(event: Event, evOld: Event,subList: List<String>){
        database.calendarQueries.updateEvent(event.Name,event.Description,event.Categoty,event.TimeStart,event.TimeEnd,event.DateStart,event.DateEnd,event.Type,event.Checked,event.Color,event.MainTaskID,evOld.Name,evOld.Categoty,evOld.Description,evOld.DateStart,evOld.Color)
    }
}