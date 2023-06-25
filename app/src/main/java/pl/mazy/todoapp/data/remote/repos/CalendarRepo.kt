package pl.mazy.todoapp.data.remote.repos

import pl.mazy.todoapp.data.LoginData
import pl.mazy.todoapp.data.interfaces.CalendarInter
import pl.mazy.todoapp.data.interfaces.TasksInter
import pl.mazy.todoapp.data.model.Event
import pl.mazy.todoapp.data.remote.TDAService
import pl.mazy.todoapp.data.remote.model.request.evS
import java.time.LocalDate
import java.util.Date

class CalendarRepo(val api: TDAService):CalendarInter {
    val loginData = LoginData
    override suspend fun addEvent(ev: Event, subList: List<String>) {
        api.newEvent(LoginData.token,createEVS(ev, subList))
    }

    override suspend fun updateEvent(ev: Event, subList: List<String>) {
        api.newEvent(LoginData.token,createEVS(ev, subList))
    }

    override suspend fun delEvent(ev: Event) {
        api.deleteEvent(LoginData.token,ev)
    }

    override suspend fun selByDate(date: LocalDate): List<Event> {
        return api.getByDate(LoginData.token,date.toString())
    }

    private fun createEVS(ev: Event,subList: List<String>):evS{
        return evS(
            id = ev.id,
            owner_id = ev.owner_id,
            name = ev.name,
            description = ev.description,
            category_id = ev.category_id,
            timeStart = ev.timeStart,
            timeEnd = ev.timeEnd,
            dateStart = ev.dateStart,
            dateEnd = ev.dateEnd,
            type = ev.type,
            checked = ev.checked,
            color = ev.color,
            mainTask_id = ev.mainTask_id,
            subList = subList
        )
    }
}