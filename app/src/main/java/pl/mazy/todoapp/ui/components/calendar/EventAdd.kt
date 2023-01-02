package pl.mazy.todoapp.ui.components.calendar

import android.widget.TimePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.SaveAs
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import pl.mazy.todoapp.Schedule
import pl.mazy.todoapp.logic.data.CalendarRepository
import pl.mazy.todoapp.logic.data.ToDoRepository
import pl.mazy.todoapp.logic.navigation.Destinations
import pl.mazy.todoapp.logic.navigation.NavController

@Composable
fun EventAdd(navController: NavController<Destinations>, sched: Schedule? = null) {

    val toDoRepository: ToDoRepository by localDI().instance()
    val focusManager = LocalFocusManager.current
    val calendarRepository: CalendarRepository by localDI().instance()
    var text by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var schedule by remember { mutableStateOf(sched) }
    val options = toDoRepository.getTusk()
    var expanded by remember { mutableStateOf(false) }
    var category:String = sched?.Categoty ?: options[0]

    if (sched != null){
        text = sched.Description
        name = sched.Name
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            OutlinedTextField(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                value = name,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                ),
                onValueChange = {
                    name = it
                    schedule= schedule?.copy(Name = it)
                },
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.moveFocus(
                        FocusDirection.Down
                    )
                }),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                label = { Text("Name") }
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier
                    .clickable { expanded = true }
                    .padding(10.dp)) {
                    Text(
                        text = category,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Icon(imageVector = Icons.Default.ExpandMore, contentDescription = null)
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        options.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption) },
                                onClick = {
                                    category = selectionOption
                                    schedule = schedule?.copy(Categoty = selectionOption)
                                    expanded = false
                                }
                            )
                        }
                    }

                }

            }
            OutlinedTextField(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .padding(5.dp),
                value = text,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                ),
                onValueChange = {
                    text = it
                    schedule= schedule?.copy(Description = it)
                },
                label = { Text("Description") }
            )


        }
        BottomAppBar {
            if (sched != null) {
                IconButton(onClick = {
                    navController.navigate(Destinations.Notes)
                    calendarRepository.deleteEvent(sched)
                }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Menu Icon")
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(modifier = Modifier.padding(10.dp)) {
                SmallFloatingActionButton(
                    onClick = {
                        if (sched == null) {
                            navController.navigate(Destinations.Notes)
                            schedule?.let { calendarRepository.addEvent(it) }
                        } else {
                            navController.navigate(Destinations.Notes)
                            schedule?.let { calendarRepository.updateEvent(it,sched) }
                        }
                    },
                    modifier = Modifier
                        .height(50.dp)
                        .width(50.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SaveAs,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}