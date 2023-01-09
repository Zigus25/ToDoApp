package pl.mazy.todoapp.logic.data

import android.annotation.SuppressLint
import android.util.Log
import pl.mazy.todoapp.Database
import pl.mazy.todoapp.Schedule
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


class CalendarRepository (
    private var database: Database
) {
    @SuppressLint("WeekBasedYear")
    fun selTwoWeek():List<Schedule>{
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val today = LocalDate.now().format( format)
        val end = LocalDate.parse(today,format).plus(2, ChronoUnit.WEEKS).format(format)
        Log.i("asd","$today,$end")
        return database.calendarQueries.selBetweenDate(today.toString(),end.toString()).executeAsList()
    }

    fun deleteEvent(schedule: Schedule){
        database.calendarQueries.deleteEvent(schedule.Name,schedule.Categoty,schedule.Description,schedule.DateStart,schedule.Color)
    }

    fun addEvent(schedule: Schedule){
        database.calendarQueries.addEvent(schedule.Name,schedule.Description,schedule.Categoty,schedule.TimeStart,schedule.TimeEnd,schedule.DateStart,schedule.DateEnd,schedule.Type,schedule.Checked,schedule.Color,schedule.SubTusk)
    }

    fun updateEvent(schedule: Schedule,scheduleOld: Schedule){
        database.calendarQueries.updateEvent(schedule.Name,schedule.Description,schedule.Categoty,schedule.TimeStart,schedule.TimeEnd,schedule.DateStart,schedule.DateEnd,schedule.Type,schedule.Checked,schedule.Color,schedule.SubTusk,scheduleOld.Name,schedule.Categoty,scheduleOld.Description,scheduleOld.DateStart,scheduleOld.Color)
    }
}