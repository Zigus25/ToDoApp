package pl.mazy.todoapp.ui.components.calendar

import android.app.DatePickerDialog
import android.graphics.Color.parseColor
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun EventAddEdit(navController: NavController<Destinations>, sched: Schedule? = null) {

    val toDoRepository: ToDoRepository by localDI().instance()
    val calendarRepository: CalendarRepository by localDI().instance()

    val focusManager = LocalFocusManager.current

    var colorPicker by remember { mutableStateOf(false) }

    val options = toDoRepository.getTusk()
    var expanded by remember { mutableStateOf(false) }
    var category:String = sched?.Categoty ?: options[0]
    var schedule by remember {
        mutableStateOf(
            sched ?:Schedule(
                "",
                "",
                options[0],
                "",
                "",
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                "",
                "#2471a3")) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.BottomCenter){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth(),
                        value = schedule.Name,
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onBackground,
                        ),
                        onValueChange = {
                            schedule = schedule.copy(Name = it)
                        },
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.moveFocus(
                                FocusDirection.Down
                            )
                        }),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        label = { Text("Name") }
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp), verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(modifier = Modifier
                            .clickable { expanded = true }
                            .padding(start = 20.dp)) {
                            Text(
                                text = category,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Icon(
                                imageVector = Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                options.forEach { selectionOption ->
                                    DropdownMenuItem(
                                        text = { Text(selectionOption) },
                                        onClick = {
                                            category = selectionOption
                                            schedule = schedule.copy(Categoty = selectionOption)
                                            expanded = false
                                        }
                                    )
                                }
                            }

                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Box(modifier = Modifier
                            .clickable { colorPicker = true }
                            .padding(end = 20.dp)) {
                            Row {
                                Text(
                                    text = "Color: ",
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Box(
                                    modifier = Modifier
                                        .height(20.dp)
                                        .width(20.dp)
                                        .background(
                                            Color(
                                                parseColor(
                                                    schedule.Color
                                                )
                                            )
                                        )
                                        .padding(start = 20.dp)
                                )
                            }
                        }
                    }
                    OutlinedTextField(
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                            .padding(5.dp),
                        value = schedule.Description,
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onBackground,
                        ),
                        onValueChange = {
                            schedule = schedule.copy(Description = it)
                        },
                        label = { Text("Description") }
                    )

                    val fCalendar = Calendar.getInstance()
                    var fYear = fCalendar.get(Calendar.YEAR)
                    var fMonth = fCalendar.get(Calendar.MONTH)
                    var fDay = fCalendar.get(Calendar.DAY_OF_MONTH)
                    fCalendar.time = Date()
                    val fDatePickerDialog = DatePickerDialog(
                        LocalContext.current,
                        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                            schedule = schedule.copy(DateStart = "${if(mDayOfMonth < 10){"0${mDayOfMonth}"}else{mDayOfMonth}}.${if(mMonth+1 < 10){"0${mMonth+1}"}else{mMonth+1}}.$mYear")
                            fYear = mYear
                            fMonth = mMonth
                            fDay = mDayOfMonth
                        }, fYear, fMonth, fDay
                    )

                    Row(modifier = Modifier.fillMaxWidth().padding(5.dp)) {
                        Box(modifier = Modifier.clickable {
                            fDatePickerDialog.show()
                        }) {
                            Text(text = schedule.DateStart, color = MaterialTheme.colorScheme.onBackground)
                        }
                    }

                    val tDatePickerDialog = DatePickerDialog(
                        LocalContext.current,
                        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                            schedule = schedule.copy(
                                DateEnd = "${
                                    if (mDayOfMonth < 10) {
                                        "0${mDayOfMonth}"
                                    } else {
                                        mDayOfMonth
                                    }
                                }.${
                                    if (mMonth + 1 < 10) {
                                        "0${mMonth + 1}"
                                    } else {
                                        mMonth + 1
                                    }
                                }.$mYear"
                            )
                        }, fYear, fMonth, fDay
                    )

                    Row(modifier = Modifier.fillMaxWidth().padding(5.dp)) {
                        Box(modifier = Modifier.clickable {
                            tDatePickerDialog.show()
                        }) {
                            Text(text = schedule.DateEnd, color = MaterialTheme.colorScheme.onBackground)
                        }
                    }
                }

                if (colorPicker) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.7F))
                        .blur(8.dp)
                        .clickable { colorPicker = false })
                    PickAColor({ colorPicker = false }, { schedule = schedule.copy(Color = it) })
                }
            }
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
                            calendarRepository.addEvent(schedule)
                        } else {
                            navController.navigate(Destinations.Notes)
                            calendarRepository.updateEvent(schedule,sched)
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

@Composable
fun PickAColor(closeAdder: () -> Unit = {},returnString: (String) -> Unit){
    val listOfColors = listOf("#5b2c6f","#884ea0","#2471a3","#17a589","#229954","#d4ac0d","#f39c12","#ba4a00","#cb4335","#839192")
    fun closeAdderAndReturn(color:Int): String {
        closeAdder()
        return listOfColors[color]
    }
    LazyVerticalGrid(modifier = Modifier
        .fillMaxWidth(),
        columns = GridCells.Adaptive(80.dp),
    ){
        for (i in listOfColors) {
            item {
                Box(
                    modifier = Modifier
                        .height(80.dp)
                        .padding(10.dp)
                        .background(
                            Color(
                                parseColor(
                                    listOfColors[listOfColors.indexOf(i)]
                                )
                            )
                        )
                        .clickable {
                            returnString(closeAdderAndReturn(listOfColors.indexOf(i)))
                        }
                )
            }
        }
    }
}