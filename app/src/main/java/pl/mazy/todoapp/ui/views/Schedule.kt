package pl.mazy.todoapp.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import pl.mazy.todoapp.Event
import pl.mazy.todoapp.logic.data.CalendarRepository
import pl.mazy.todoapp.logic.navigation.Destinations
import pl.mazy.todoapp.logic.navigation.NavController
import pl.mazy.todoapp.ui.components.calendar.schedule.DateShow
import pl.mazy.todoapp.ui.components.calendar.schedule.SingleEvent
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun Schedule(
    navController: NavController<Destinations>,
) {
    val calendarRepository: CalendarRepository by localDI().instance()
    var events: List<Event>? by remember { mutableStateOf(null) }
    val scope = rememberCoroutineScope()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date: LocalDate = LocalDate.parse(LocalDate.now().format(formatter),formatter)
    val maxDate = if (calendarRepository.selMaxDate() != null) {
        LocalDate.parse(calendarRepository.selMaxDate(), formatter)
    } else {
        date
    }

    fun listDates(now:LocalDate,max:LocalDate):List<LocalDate> = List(now.until(max).days+1){now.plusDays(it.toLong())}

    fun loadEvents() = scope.launch {
        events = calendarRepository.selEvents()
    }
    loadEvents()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        LazyColumn(modifier = Modifier.weight(1f)) {
            if (events != null) {
                items(listDates(date,maxDate)){
                    DateShow(dateD = it.toString())
                    events!!.forEach { ev ->
                        if (ev.DateEnd != null) {
                            if (LocalDate.parse(ev.DateEnd, formatter) >= it && LocalDate.parse(ev.DateStart, formatter) <= it) {
                                SingleEvent(navController = navController, event = ev, it)
                            }
                        } else {
                            SingleEvent(navController = navController, event = ev)
                        }
                    }
                }
            } else {
                item { DateShow(LocalDate.now().format(formatter).toString()) }
            }
        }
        BottomAppBar {
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(Icons.Filled.Menu, contentDescription = "Menu Icon")
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(modifier = Modifier.weight(1f)) {
                SmallFloatingActionButton(
                    onClick = { navController.navigate(Destinations.EventAdd(null, false)) },
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
