package pl.mazy.todoapp.logic.data

import android.annotation.SuppressLint
import pl.mazy.todoapp.Database
import pl.mazy.todoapp.Event
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


class CalendarRepository (
    private var database: Database
) {
    @SuppressLint("WeekBasedYear")
    fun selEvents():List<Event>{
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val today = LocalDate.now().format( format)
        val end = LocalDate.parse(today,format).plus(4, ChronoUnit.WEEKS).format(format)
        return database.calendarQueries.selBetweenDate(today.toString(),end.toString()).executeAsList()
    }

    fun deleteEvent(ev: Event){
        database.calendarQueries.deleteEvent(ev.Name,ev.Categoty,ev.Description,ev.DateStart,ev.Color)
    }

    fun addEvent(ev: Event){
        database.calendarQueries.addEvent(ev.Name,ev.Description,ev.Categoty,ev.TimeStart,ev.TimeEnd,ev.DateStart,ev.DateEnd,ev.Type,ev.Checked,ev.Color,ev.SubTusk,ev.MainTaskID)
    }

    fun updateEvent(event: Event, evOld: Event){
        database.calendarQueries.updateEvent(event.Name,event.Description,event.Categoty,event.TimeStart,event.TimeEnd,event.DateStart,event.DateEnd,event.Type,event.Checked,event.Color,event.SubTusk,evOld.Name,evOld.Categoty,evOld.Description,evOld.DateStart,evOld.Color)
    }
}