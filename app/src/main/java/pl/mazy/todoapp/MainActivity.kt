package pl.mazy.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            withDI((application as ToDoApplication).di) {
                ToDoAPpTheme {
                    var expandedMe by remember {
                        mutableStateOf(false)
                    }
                    var expandedP by remember {
                        mutableStateOf(false)
                    }
                    var program by remember {
                        mutableStateOf("Task List")
                    }
                    val controller: NavController<Destinations> by remember {
                        mutableStateOf(NavController(Destinations.TaskList))
                    }
                    BackHandler(!controller.isLast()) {
                        controller.pop()
                    }
                    Column {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background), verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { /* doSomething() */ }) {
                                IconButton(onClick = { expandedMe = true }) {
                                    Icon(Icons.Filled.Menu, contentDescription = "Menu Icon",
                                        tint = MaterialTheme.colorScheme.onBackground)
                                }
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Row {
                                Text(text = program,modifier = Modifier.clickable { expandedP = true }, color = MaterialTheme.colorScheme.onBackground)
                                Icon(
                                    imageVector = Icons.Default.ExpandMore,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                                DropdownMenu(
                                    expanded = expandedP,
                                    onDismissRequest = { expandedP = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Task List") },
                                        onClick = {
                                            controller.navigate(Destinations.TaskList)
                                            program = "Task List"
                                            expandedP = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Notes") },
                                        onClick = {
                                            controller.navigate(Destinations.Notes)
                                            program = "Notes"
                                            expandedP = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Calendar") },
                                        onClick = {
                                            controller.navigate(Destinations.Schedule)
                                            program = "Calendar"
                                            expandedP = false
                                        }
                                    )

                                }
                            }
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
            }
        }
    }
}