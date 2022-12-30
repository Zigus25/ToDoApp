package pl.mazy.todoapp.ui.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.mazy.todoapp.logic.data.ScheduleRepository
import pl.mazy.todoapp.logic.navigation.Destinations
import pl.mazy.todoapp.logic.navigation.NavController
import pl.mazy.todoapp.ui.components.CalendarLayout
import pl.mazy.todoapp.ui.components.events.SingleEvent
import pl.mazy.todoapp.ui.theme.ToDoAPpTheme
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Schedule(
    navController: NavController<Destinations>,
){
    ToDoAPpTheme {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)) {
            Row(
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())) {
                Box(
                    Modifier
                        .width(60.dp)) {
                    for (i in 0..24){
                        Text(
                            text = if (i < 10){"0$i:00"}else "$i:00",
                            modifier = Modifier.padding(top = (180*i).dp),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
                CalendarLayout(
                    Modifier
                        .weight(1f)) {
                    aaa( pl.mazy.todoapp.Schedule(
                                "Sample Event",
                                "Lorem Ipsum this is my test description",
                                "01:00",
                                "02:00",
                                "12",
                                "0",
                                "#ff0012"
                            ))

                    aaa( pl.mazy.todoapp.Schedule(
                        "Sample Event",
                        "Lorem Ipsum this is my test description",
                        "01:00",
                        "02:00",
                        "12",
                        "0",
                        "#00ff12"
                    ))
                    aaa( pl.mazy.todoapp.Schedule(
                        "Sample Event",
                        "Lorem Ipsum this is my test description",
                        "01:20",
                        "01:40",
                        "12",
                        "0",
                        "#ffff12"
                    ))
                }
            }
            BottomAppBar {
                IconButton(onClick = { /* doSomething() */ }) {
                    Icon(Icons.Filled.Menu, contentDescription = "Menu Icon")
                }
                Spacer(modifier = Modifier.weight(1f))
                Box(modifier = Modifier.weight(1f)) {
                    SmallFloatingActionButton(
                        onClick = { navController.navigate(Destinations.CreateNote) },
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
}