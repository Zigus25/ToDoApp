package pl.mazy.todoapp.ui.components.calendar

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
@Composable
fun DateShow(dateD: String) {
    val days = listOf("SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY")
    val months = listOf("JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY","AUGUST","SEPTEMBER","OCTOBER","NOVEMBER","DECEMBER")

    val calendar: Calendar = Calendar.getInstance()
    SimpleDateFormat("dd.MM.yyyy").parse(dateD)?.let { calendar.time = it }
    Text(text = "${days[calendar.get(Calendar.DAY_OF_WEEK)]}, ${calendar.get(Calendar.DAY_OF_MONTH)} ${months[calendar.get(Calendar.MONTH)]}",
        fontSize = 20.sp,
        modifier = Modifier.padding(start = 10.dp, top = 20.dp, bottom = 10.dp),
        color = MaterialTheme.colorScheme.onBackground)
}