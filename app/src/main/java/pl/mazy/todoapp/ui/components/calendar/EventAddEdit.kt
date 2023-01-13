@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package pl.mazy.todoapp.ui.components.calendar

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color.parseColor
import android.widget.DatePicker
import android.widget.Toast
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
import pl.mazy.todoapp.Event
import pl.mazy.todoapp.logic.data.CalendarRepository
import pl.mazy.todoapp.logic.data.ToDoRepository
import pl.mazy.todoapp.logic.navigation.Destinations
import pl.mazy.todoapp.logic.navigation.NavController
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventAddEdit(navController: NavController<Destinations>, ev: Event?,isTask:Boolean) {

    val toDoRepository: ToDoRepository by localDI().instance()
    val calendarRepository: CalendarRepository by localDI().instance()

    val defaultDate = "1970-01-01"

    val focusManager = LocalFocusManager.current
    val formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val formatTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    var colorPicker by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()
    val options = toDoRepository.getTusk()
    var expanded by remember { mutableStateOf(false) }
    var event by remember {
        mutableStateOf(
            ev?:Event(
                "",
                "",
                options[0],
                "$defaultDate ${if(calendar[Calendar.HOUR_OF_DAY]<10){"0${calendar[Calendar.HOUR_OF_DAY]}"}else{calendar[Calendar.HOUR_OF_DAY]}}:00",
                "$defaultDate ${if (calendar[Calendar.HOUR_OF_DAY]+1 < 10) {"0${calendar[Calendar.HOUR_OF_DAY]+1}"}else{calendar[Calendar.HOUR_OF_DAY]+1}}:00",
                LocalDate.now().format(formatDate),
                LocalDate.now().format(formatDate),
                isTask,
                false,
                "#2471a3",
                false,
                null
            )) }


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
                        value = event.Name,
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onBackground,
                        ),
                        onValueChange = {
                            event = event.copy(Name = it)
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
                                text = event.Categoty,
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
                                            event = event.copy(Categoty = selectionOption)
                                            expanded = false
                                        }
                                    )
                                }
                            }

                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "task:", color = MaterialTheme.colorScheme.onBackground)
                            Checkbox(
                                checked = event.Type,
                                onCheckedChange = {event = event.copy(Type = !event.Type)
                                }
                            )
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
                                                    event.Color
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
                        value = event.Description,
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onBackground,
                        ),
                        onValueChange = {
                            event = event.copy(Description = it)
                        },
                        label = { Text("Description") }
                    )

                    val fYear = calendar.get(Calendar.YEAR)
                    val fMonth = calendar.get(Calendar.MONTH)
                    val fDay = calendar.get(Calendar.DAY_OF_MONTH)
                    calendar.time = Date()
                    val fDatePickerDialog = DatePickerDialog(
                        LocalContext.current,
                        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                            event = event.copy(DateStart = "$mYear-${if(mMonth+1 < 10){"0${mMonth+1}"}else{mMonth+1}}-${if(mDayOfMonth < 10){"0${mDayOfMonth}"}else{mDayOfMonth}}")
                            if(LocalDate.parse(event.DateEnd,formatDate)<LocalDate.parse(event.DateStart,formatDate)){
                                event = event.copy(DateEnd = event.DateStart)
                            }
                        }, fYear, fMonth, fDay
                    )

                    val timeF = LocalDateTime.parse(event.TimeStart,formatTime)
                    val fTimePickerDialog = TimePickerDialog(
                        LocalContext.current,
                        {_, mHour:Int, mMinute:Int ->
                            event = event.copy(TimeStart = "$defaultDate ${if (mHour<10){"0$mHour"}else{mHour}}:${if (mMinute<10){"0$mMinute"}else{mMinute}}")
                        },timeF.hour,timeF.minute,true
                    )

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)) {
                        Box(modifier = Modifier.clickable {
                            fDatePickerDialog.show()
                        }) {
                            Text(text = event.DateStart, color = MaterialTheme.colorScheme.onBackground)
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Box(modifier = Modifier.clickable {
                            fTimePickerDialog.show()
                        }){
                            Text(text = event.TimeStart.takeLast(5), color = MaterialTheme.colorScheme.onBackground)
                        }
                    }

                    val tDatePickerDialog = DatePickerDialog(
                        LocalContext.current,
                        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                            event = event.copy(
                                DateEnd = "$mYear-${
                                    if (mMonth + 1 < 10) {
                                        "0${mMonth + 1}"
                                    } else {
                                        mMonth + 1
                                    }
                                }-${
                                    if (mDayOfMonth < 10) {
                                        "0${mDayOfMonth}"
                                    } else {
                                        mDayOfMonth
                                    }
                                }"
                            )
                        }, fYear, fMonth, fDay
                    )

                    val timeT = LocalDateTime.parse(event.TimeEnd,formatTime)
                    val tTimePickerDialog = TimePickerDialog(
                        LocalContext.current,
                        {_, mHour:Int, mMinute:Int ->
                            event = event.copy(TimeEnd = "$defaultDate ${if (mHour<10){"0$mHour"}else{mHour}}:${if (mMinute<10){"0$mMinute"}else{mMinute}}")
                        },timeT.hour,timeT.minute,true
                    )

                    val minDate = LocalDate.parse(event.DateStart,formatDate)
                    calendar.set(minDate.year,minDate.monthValue-1,minDate.dayOfMonth)
                    tDatePickerDialog.datePicker.updateDate(minDate.year,minDate.monthValue-1,minDate.dayOfMonth)

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)) {
                        Box(modifier = Modifier.clickable {
                            tDatePickerDialog.datePicker.minDate = calendar.timeInMillis
                            tDatePickerDialog.show()
                        }) {
                            Text(text = event.DateEnd, color = MaterialTheme.colorScheme.onBackground)
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Box(modifier = Modifier.clickable {
                            tTimePickerDialog.show()
                        }){
                            Text(text = event.TimeEnd.takeLast(5), color = MaterialTheme.colorScheme.onBackground)
                        }
                    }
                    if (event.Type){
                        Column {
                            //TODO Adding sub task to list
                            //TODO showing sub tasks from generated list
                        }
                    }
                }

                if (colorPicker) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.7F))
                        .blur(8.dp)
                        .clickable { colorPicker = false })
                    PickAColor({ colorPicker = false }, { event = event.copy(Color = it) })
                }
            }
        }
        BottomAppBar {
            if (ev != null) {
                IconButton(onClick = {
                    navController.navigate(if (isTask){Destinations.TaskList}else{Destinations.Schedule})
                    calendarRepository.deleteEvent(ev)
                }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Menu Icon")
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            if (LocalDate.parse(event.DateEnd,formatDate)!=LocalDate.parse(event.DateStart,formatDate)||LocalDate.parse(event.DateEnd,formatDate)==LocalDate.parse(event.DateStart,formatDate)&&LocalDateTime.parse(event.TimeEnd,formatTime)>LocalDateTime.parse(event.TimeStart,formatTime)) {
                Box(modifier = Modifier.padding(10.dp)) {
                    SmallFloatingActionButton(
                        onClick = {
                            if (ev == null) {
                                navController.navigate(
                                    if (isTask) {
                                        Destinations.TaskList
                                    } else {
                                        Destinations.Schedule
                                    }
                                )
                                calendarRepository.addEvent(event)
                            } else {
                                navController.navigate(
                                    if (isTask) {
                                        Destinations.TaskList
                                    } else {
                                        Destinations.Schedule
                                    }
                                )
                                calendarRepository.updateEvent(event, ev)
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
            }else{
                Toast.makeText(LocalContext.current, "Not valid DATE or TIME", Toast.LENGTH_LONG).show()
            }
        }
    }
}