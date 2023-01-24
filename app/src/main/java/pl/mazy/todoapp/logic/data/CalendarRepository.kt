package pl.mazy.todoapp.logic.data

import android.annotation.SuppressLint
import android.util.Log
import pl.mazy.todoapp.Database
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
    }

    fun addEvent(ev: Event,subList:List<String>){
        database.calendarQueries.addEvent(ev.Name,ev.Description,ev.Category,ev.TimeStart,ev.TimeEnd,ev.DateStart,ev.DateEnd,ev.Type,ev.Checked,ev.Color,ev.MainTaskID)
        val id = database.calendarQueries.selMyID().executeAsOne().max
        subList.forEach{
            database.calendarQueries.addEvent(it,"",ev.Category,null,null,null,null, Type = true, Checked = false, ev.Color,id)

        }
    }

    fun updateEvent(event: Event, evOld: Event,subList: List<String>){
        database.calendarQueries.updateEvent(event.Name,event.Description,event.Category,event.TimeStart,event.TimeEnd,event.DateStart,event.DateEnd,event.Type,event.Checked,event.Color,event.MainTaskID,evOld.Name,evOld.Category,evOld.Description,evOld.DateStart,evOld.Color)
        val size:Int = selectSubList(event.id).size
        for (index in size until subList.size){
            Log.i("",index.toString())
            database.calendarQueries.addEvent(subList[index],"",event.Category,null,null,null,null, Type = true, Checked = false, event.Color,event.id)
        }
    }

    fun selectSubList(id:Long):List<String>{
        return database.calendarQueries.selNameByID(id).executeAsList()
    }
}