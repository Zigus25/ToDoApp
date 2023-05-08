package pl.mazy.todoapp.data.interfaces

import pl.mazy.todoapp.data.model.Event
import java.time.LocalDate
import java.util.Date

interface CalendarInter {
    suspend fun addEvent(ev:Event,subList: List<String>)
    suspend fun updateEvent(ev: Event,subList:List<String>)
    suspend fun delEvent(ev: Event)
    suspend fun namesSubList(id:Int):List<String>
    suspend fun selByDate(date: LocalDate):List<Event>
}