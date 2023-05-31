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
import pl.mazy.todoapp.data.LoginData
import pl.mazy.todoapp.data.interfaces.CalendarInter
import pl.mazy.todoapp.data.local.CalendarRepoLocal
import pl.mazy.todoapp.data.model.Event
import pl.mazy.todoapp.data.remote.repos.CalendarRepo
import pl.mazy.todoapp.navigation.Destinations
import pl.mazy.todoapp.navigation.NavController
import pl.mazy.todoapp.ui.components.calendar.schedule.DateShow
import pl.mazy.todoapp.ui.components.calendar.schedule.SingleEvent
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Schedule(
    navController: NavController<Destinations>,
) {
    val calRepo:CalendarInter = if (LoginData.token==""){
        val caR:CalendarRepoLocal by localDI().instance()
        caR
    }else{
        val caR:CalendarRepo by localDI().instance()
        caR
    }
    var events: List<Event> by remember { mutableStateOf(listOf()) }
    val scope = rememberCoroutineScope()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date: LocalDate = LocalDate.parse(LocalDate.now().format(formatter),formatter)
    var maxDate:LocalDate = date

    fun listDates(now:LocalDate,max:LocalDate):List<LocalDate> = List(now.until(max).days+1){now.plusDays(it.toLong())}
    fun foundMaxDate(list:List<Event>):LocalDate{
        return if (list.isNotEmpty()) {
            LocalDate.parse(list[list.lastIndex].dateEnd, formatter)
        }else{
            date.plusDays(2)
        }
    }

    fun loadEvents() = scope.launch {
        events = calRepo.selByDate(date)
    }
    LaunchedEffect(events,LoginData.login){
        scope.launch {
            loadEvents()
        }
        maxDate = foundMaxDate(events)
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Destinations.EventAdd(null, false,null)) },
                modifier = Modifier
                    .height(50.dp)
                    .width(50.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            LazyColumn(modifier = Modifier.weight(1f)) {
                if (events.isNotEmpty()) {
                    items(listDates(date, maxDate)) {
                        val e = events.filter { ev ->
                            ev.dateEnd != null && LocalDate.parse(
                                ev.dateEnd,
                                formatter
                            ) >= it && LocalDate.parse(ev.dateStart, formatter) <= it
                        }
                        if (e.isNotEmpty() || it == date)
                            DateShow(dateD = it.toString())
                        e.forEach { ev ->
                            SingleEvent(navController = navController, event = ev, it)
                        }
                    }
                } else {
                    item { DateShow(LocalDate.now().format(formatter).toString()) }
                }
                item {
                    Spacer(modifier = Modifier.padding(30.dp))
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Box(modifier = Modifier.padding(15.dp)) {

                }
            }
        }
    }
}
