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
        api.newEvent(LoginData.token,ev)
        TODO("subtask adding")
    }

    override suspend fun updateEvent(ev: Event, subList: List<String>) {
        api.newEvent(LoginData.token,ev)
    }

    override suspend fun delEvent(ev: Event) {
        api.deleteEvent(LoginData.token,ev)
    }

    override suspend fun selByDate(date: LocalDate): List<Event> {
        return api.getByDate(LoginData.token,date.toString())
    }
}