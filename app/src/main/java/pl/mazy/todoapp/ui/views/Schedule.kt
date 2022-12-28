package pl.mazy.todoapp.ui.views

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.mazy.todoapp.logic.navigation.Destinations
import pl.mazy.todoapp.logic.navigation.NavController
import pl.mazy.todoapp.ui.components.events.SingleEvent
import pl.mazy.todoapp.ui.theme.ToDoAPpTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Schedule(
    navController: NavController<Destinations>,
){
    ToDoAPpTheme {
        Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            LazyColumn(modifier = Modifier.weight(1f)){
                item {
                    SingleEvent(pl.mazy.todoapp.Schedule("Sample Event","Lorem Ipsum this is my test description","09:00","10:00","12","0","#35fa12"))
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