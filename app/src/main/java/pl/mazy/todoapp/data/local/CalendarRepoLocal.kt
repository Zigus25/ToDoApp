package pl.mazy.todoapp.data.local

import pl.mazy.todoapp.Database
import pl.mazy.todoapp.data.interfaces.CalendarInter
import pl.mazy.todoapp.data.model.Event
import java.time.LocalDate
import java.util.Date

class CalendarRepoLocal(
    private var database: Database
):CalendarInter {
    override suspend fun addEvent(ev: Event, subList: List<String>) {
        TODO("Not yet implemented")
    }

    override suspend fun updateEvent(ev: Event, subList: List<String>) {
        TODO("Not yet implemented")
    }

    override suspend fun delEvent(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun namesSubList(id: Int): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun selByDate(date: LocalDate): List<Event> {
        TODO("Not yet implemented")
    }
}