package pl.mazy.todoapp.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
){
    val calendarRepository: CalendarRepository by localDI().instance()
    var events: List<Event>? by remember { mutableStateOf(null) }
    val scope = rememberCoroutineScope()
    var date:String = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString()

    fun loadEvents() = scope.launch {
        events = calendarRepository.selEvents()
    }
    loadEvents()
    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {
        LazyColumn(modifier = Modifier.weight(1f)){
            item { DateShow(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString()) }
            if (events!=null) {
                for (ev in events!!) {
                    if (ev.DateStart!=date){
                        date = ev.DateStart.toString()
                        item { ev.DateStart?.let { DateShow(it) } }
                    }
                    item {
                        SingleEvent(navController,event = ev)
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
                    onClick = { navController.navigate(Destinations.EventAdd(null,false)) },
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
