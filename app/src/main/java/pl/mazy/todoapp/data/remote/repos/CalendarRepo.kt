package pl.mazy.todoapp.data.remote.repos

import pl.mazy.todoapp.data.LoginData
import pl.mazy.todoapp.data.interfaces.CalendarInter
import pl.mazy.todoapp.data.interfaces.TasksInter
import pl.mazy.todoapp.data.model.Event
import pl.mazy.todoapp.data.remote.TDAService
import java.time.LocalDate
import java.util.Date

class CalendarRepo(val api: TDAService):CalendarInter {
    val loginData = LoginData
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