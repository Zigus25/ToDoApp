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
import pl.mazy.todoapp.Event
import pl.mazy.todoapp.logic.navigation.Destinations
import pl.mazy.todoapp.logic.navigation.NavController

@SuppressLint("SuspiciousIndentation")
@Composable
fun SingleEvent(navController: NavController<Destinations>, event:Event){
    Column(modifier = Modifier
        .height(75.dp)
        .fillMaxWidth()
        .padding(10.dp)
        .clip(RoundedCornerShape(5.dp))
        .background(
            Color(parseColor(event.Color))
        )
        .clickable {
            navController.navigate(Destinations.EventAdd(event,false))
        }
    ) {
        Text(text = event.Name, fontSize = 24.sp)
        Row {
            Spacer(modifier = Modifier.weight(1f))
            if (event.TimeStart!=null&&event.TimeEnd!=null)
            Text(text = "${event.TimeStart.takeLast(5)} - ${event.TimeEnd.takeLast(5)}")
        }
    }
}