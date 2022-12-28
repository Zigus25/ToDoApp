package pl.mazy.todoapp.ui.components.events

import android.graphics.Color.parseColor
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.mazy.todoapp.Schedule
import pl.mazy.todoapp.ui.theme.ToDoAPpTheme
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SingleEvent(schedule:Schedule){
    ToDoAPpTheme {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val time1 = LocalTime.parse(schedule.TimeStart, formatter)
        val time2 = LocalTime.parse(schedule.TimeEnd, formatter)
        val duration = (time1.until(time2, ChronoUnit.MINUTES)).toInt()
        Column(modifier = Modifier.height((duration*2).dp)) {
            Row(modifier = Modifier
                .fillMaxSize()
                .padding(start = 15.dp, end = 15.dp)
                .background(
                    Color(parseColor(schedule.Color))
                )) {
                Text(text = schedule.Name)
            }
        }
    }
}