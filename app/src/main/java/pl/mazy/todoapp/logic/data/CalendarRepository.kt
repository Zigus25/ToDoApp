package pl.mazy.todoapp.logic.data

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import pl.mazy.todoapp.Database
import pl.mazy.todoapp.Schedule
import java.time.LocalDate
import java.time.temporal.ChronoUnit


class CalendarRepository (
    private var database: Database
) {
    @SuppressLint("WeekBasedYear")
    fun selTwoWeek():List<Schedule>{
        return database.calendarQueries.selBetweenDate().executeAsList()
    }

    fun deleteEvent(schedule: Schedule){
        database.calendarQueries.deleteEvent(schedule.Name,schedule.Categoty,schedule.Description,schedule.DateStart,schedule.Color)
    }

    fun addEvent(schedule: Schedule){
        database.calendarQueries.addEvent(schedule.Name,schedule.Description,schedule.Categoty,schedule.TimeStart,schedule.TimeEnd,schedule.DateStart,schedule.DateEnd,schedule.Type,schedule.Color)
    }

    fun updateEvent(schedule: Schedule,scheduleOld: Schedule){
        database.calendarQueries.updateEvent(schedule.Name,schedule.Description,schedule.Categoty,schedule.TimeStart,schedule.TimeEnd,schedule.DateStart,schedule.DateEnd,schedule.Type,schedule.Color,scheduleOld.Name,schedule.Categoty,scheduleOld.Description,scheduleOld.DateStart,scheduleOld.Color)
    }
}