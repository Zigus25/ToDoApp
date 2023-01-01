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
    @RequiresApi(Build.VERSION_CODES.O)
    fun selTwoWeek():List<Schedule>{
        val today = LocalDate.now()
        val end = today.plus(2, ChronoUnit.WEEKS)
        return database.calendarQueries.selBetweenDate(today.toString(),end.toString()).executeAsList()
    }
}