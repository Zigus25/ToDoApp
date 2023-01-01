package pl.mazy.todoapp.ui.components.calendar

import android.widget.TimePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.SaveAs
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
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
import pl.mazy.todoapp.logic.navigation.Destinations
import pl.mazy.todoapp.logic.navigation.NavController

@Composable
fun EventAdd(navController: NavController<Destinations>, sched: Schedule? = null) {

    val focusManager = LocalFocusManager.current
    val calendarRepository: CalendarRepository by localDI().instance()
    var text by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var schedule by remember { mutableStateOf(sched) }

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
                    schedule!!.Name = it
                },
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.moveFocus(
                        FocusDirection.Down
                    )
                }),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                label = { Text("Name") }
            )

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
                    schedule!!.Description = it
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