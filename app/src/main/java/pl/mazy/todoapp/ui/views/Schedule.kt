package pl.mazy.todoapp.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import pl.mazy.todoapp.Schedule
import pl.mazy.todoapp.logic.data.CalendarRepository
import pl.mazy.todoapp.logic.navigation.Destinations
import pl.mazy.todoapp.logic.navigation.NavController
import pl.mazy.todoapp.ui.components.calendar.schedule.SingleEvent
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun Schedule(
    navController: NavController<Destinations>,
){
    val calendarRepository: CalendarRepository by localDI().instance()
    var events: List<Schedule>? by remember { mutableStateOf(null) }
    val scope = rememberCoroutineScope()
    var date:String? = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")).toString()
    var dateD = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy"))

    fun loadEvents() = scope.launch {
        events = calendarRepository.selTwoWeek()
    }
    loadEvents()
    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {
        LazyColumn(modifier = Modifier.weight(1f)){
            item { DateShow(dateD) }
            if (events!=null) {
                for (ev in events!!) {
                    if (ev.DateStart!=date){
                        date = ev.DateStart
                        dateD = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                        item { DateShow(dateD) }
                    }
                    item {
                        SingleEvent(navController,schedule = ev)
                    }
                }
            }
        }
        BottomAppBar {
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(Icons.Filled.Menu, contentDescription = "Menu Icon")
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(modifier = Modifier.weight(1f)) {
                SmallFloatingActionButton(
                    onClick = { navController.navigate(Destinations.EventAdd(null)) },
                    modifier = Modifier
                        .height(50.dp)
                        .width(50.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                    )
                }
            }

            IconButton(onClick = { navController.navigate(Destinations.TaskList) }) {
                Icon(
                    Icons.Filled.Checklist,
                    contentDescription = "Calendar Icon",
                )
            }
            IconButton(onClick = { navController.navigate(Destinations.Notes) }) {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = "Calendar Icon",
                )
            }
        }
    }
}
@Composable
fun DateShow(dateD: LocalDate) {
    Text(text = dateD.dayOfWeek.toString()+", "+dateD.dayOfMonth.toString()+" "+dateD.month.toString(),
        fontSize = 20.sp,
        modifier = Modifier.padding(start = 10.dp, top = 20.dp, bottom = 10.dp),
        color = MaterialTheme.colorScheme.onBackground)
}