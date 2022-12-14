package pl.mazy.todoapp.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.mazy.todoapp.logic.navigation.Destinations
import pl.mazy.todoapp.logic.navigation.NavController

@Composable
fun Schedule(
    navController: NavController<Destinations>,
){
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.weight(1f)){

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