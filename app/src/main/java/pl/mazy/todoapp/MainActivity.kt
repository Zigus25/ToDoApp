package pl.mazy.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.kodein.di.compose.withDI
import pl.mazy.todoapp.logic.navigation.Destinations
import pl.mazy.todoapp.logic.navigation.NavController
import pl.mazy.todoapp.ui.components.calendar.EventAddEdit
import pl.mazy.todoapp.ui.components.note.NoteAdding
import pl.mazy.todoapp.ui.theme.ToDoAPpTheme
import pl.mazy.todoapp.ui.views.NoteList
import pl.mazy.todoapp.ui.views.Schedule
import pl.mazy.todoapp.ui.views.TaskList

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            withDI((application as ToDoApplication).di) {
                ToDoAPpTheme {
                    var program by remember {
                        mutableStateOf("Task List")
                    }
                    val controller: NavController<Destinations> by remember {
                        mutableStateOf(NavController(Destinations.TaskList))
                    }
                    BackHandler(!controller.isLast()) {
                        controller.pop()
                    }
                    var drawerState = rememberDrawerState(DrawerValue.Closed)
                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = {
                            Spacer(Modifier.height(12.dp))
                            NavigationDrawerItem(
                                label = { Text("Task List") },
                                selected = program == "Task List",
                                onClick = {
                                    program = "Task List"
                                    drawerState = DrawerState(DrawerValue.Closed)
                                    controller.navigate(Destinations.TaskList)
                                },
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                            )
                            NavigationDrawerItem(
                                label = { Text("Notes") },
                                selected = program == "Notes",
                                onClick = {
                                    program = "Notes"
                                    drawerState = DrawerState(DrawerValue.Closed)
                                    controller.navigate(Destinations.Notes)
                                },
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                            )
                            NavigationDrawerItem(
                                label = { Text("Calendar") },
                                selected = program == "Calendar",
                                onClick = {
                                    program = "Calendar"
                                    drawerState = DrawerState(DrawerValue.Closed)
                                    controller.navigate(Destinations.Schedule)
                                },
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                            )
                        },
                        content = {
                            Column {
                                Row(modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.background), verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = {
                                        drawerState = DrawerState(DrawerValue.Open)
                                    }) {
                                        Icon(Icons.Filled.Menu, contentDescription = "Menu Icon", tint = MaterialTheme.colorScheme.onBackground)
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(text = program, fontSize = 24.sp, color = MaterialTheme.colorScheme.onBackground)
                                    Spacer(modifier = Modifier.weight(1f))
                                    IconButton(onClick = {  }) {
                                        Icon(
                                            Icons.Filled.SupervisedUserCircle,
                                            contentDescription = "Profile icon",
                                            tint = MaterialTheme.colorScheme.onBackground
                                        )
                                    }
                                }
                                Box(modifier = Modifier.weight(1f)) {
                                    when (val x = controller.currentBackStackEntry.value) {
                                        is Destinations.TaskList -> TaskList(controller)
                                        is Destinations.NoteDetails -> NoteAdding(controller,x.name,x.des)
                                        is Destinations.Notes -> NoteList(controller)
                                        is Destinations.Schedule -> Schedule(controller)
                                        is Destinations.EventAdd -> EventAddEdit(controller,x.event,x.isTask)
                                        is Destinations.CreateNote -> NoteAdding(controller,"","")
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}