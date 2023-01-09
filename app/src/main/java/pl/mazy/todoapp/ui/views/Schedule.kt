package pl.mazy.todoapp.ui.views

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import pl.mazy.todoapp.ui.components.calendar.DateShow
import pl.mazy.todoapp.ui.components.calendar.schedule.SingleEvent
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun Schedule(
    navController: NavController<Destinations>,
){
    val calendarRepository: CalendarRepository by localDI().instance()
    var events: List<Schedule>? by remember { mutableStateOf(null) }
    val scope = rememberCoroutineScope()
    var date:String = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")).toString()

    fun loadEvents() = scope.launch {
        events = calendarRepository.selTwoWeek()
    }
    loadEvents()
    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {
        LazyColumn(modifier = Modifier.weight(1f)){
            item { DateShow(date) }
            if (events!=null) {
                for (ev in events!!) {
                    if (ev.DateStart!=date){
                        date = ev.DateStart
                        item { DateShow(date) }
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
