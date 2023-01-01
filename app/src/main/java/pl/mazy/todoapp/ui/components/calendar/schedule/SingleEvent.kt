package pl.mazy.todoapp.ui.components.calendar.schedule

import android.graphics.Color.parseColor
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.mazy.todoapp.Schedule
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SingleEvent(schedule:Schedule){
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val time1 = LocalTime.parse(schedule.TimeStart, formatter)
        val time2 = LocalTime.parse(schedule.TimeEnd, formatter)
        val duration = (time1.until(time2, ChronoUnit.MINUTES)).toInt()
        Box(modifier = Modifier
            .height((duration * 3).dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(
                Color(parseColor(schedule.Color))
            )
        ){
            Text(text = schedule.Name)
        }
}