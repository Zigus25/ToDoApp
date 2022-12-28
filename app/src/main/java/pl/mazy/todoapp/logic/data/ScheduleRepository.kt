package pl.mazy.todoapp.logic.data

import pl.mazy.todoapp.Database
import pl.mazy.todoapp.Schedule

class ScheduleRepository (
    private var database: Database
) {
    fun selFromDay(day:String):List<Schedule>{
        return database.scheduleQueries.selFromDay(day).executeAsList()
    }
}