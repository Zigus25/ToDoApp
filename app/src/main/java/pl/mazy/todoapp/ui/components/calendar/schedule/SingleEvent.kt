package pl.mazy.todoapp.ui.components.calendar.schedule

import android.annotation.SuppressLint
import android.graphics.Color.parseColor
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.mazy.todoapp.data.model.Event
import pl.mazy.todoapp.navigation.Destinations
import pl.mazy.todoapp.navigation.NavController
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SuppressLint("SuspiciousIndentation")
@Composable
fun SingleEvent(navController: NavController<Destinations>, event: Event, dateNow: LocalDate? = null){
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    Column(modifier = Modifier
        .height(75.dp)
        .fillMaxWidth()
        .padding(10.dp)
        .clip(RoundedCornerShape(5.dp))
        .background(
            Color(parseColor(event.color))
        )
        .clickable {
            navController.navigate(Destinations.EventAdd(event,event.type))
        }
    ) {
        Text(text = event.name, fontSize = 24.sp)
        Row {
            Spacer(modifier = Modifier.weight(1f))
            if (event.timeStart!=null&&event.timeEnd!=null) {
                if (LocalDate.parse(event.dateStart,formatter)==LocalDate.parse(event.dateEnd,formatter)) {
                    Text(text = "${event.timeStart.takeLast(5)} - ${event.timeEnd.takeLast(5)}")
                }else{
                    if (LocalDate.parse(event.dateStart,formatter)==dateNow){
                        Text(text = event.timeStart.takeLast(5))
                    }else if (LocalDate.parse(event.dateEnd,formatter)==dateNow){
                        Text(text = event.timeEnd.takeLast(5))
                    }
                }
            }
        }
    }
}