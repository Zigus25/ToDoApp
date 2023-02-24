package pl.mazy.todoapp.ui.components.calendar

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color.parseColor
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import pl.mazy.todoapp.logic.data.repos.CalendarRepository
import pl.mazy.todoapp.logic.data.Event
import pl.mazy.todoapp.logic.data.LoginData
import pl.mazy.todoapp.logic.data.repos.ToDoRepository
import pl.mazy.todoapp.logic.navigation.Destinations
import pl.mazy.todoapp.logic.navigation.NavController
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventAddEdit(navController: NavController<Destinations>, ev: Event?, isTask:Boolean) {

    val toDoRepository: ToDoRepository by localDI().instance()
    val calendarRepository: CalendarRepository by localDI().instance()

    var canAddSL by remember { mutableStateOf(true) }

    val subList = remember { mutableListOf<String>() }
    if (ev!=null&&canAddSL){
        calendarRepository.selectSubList(ev.id).forEach{
            subList.add(it)
        }
        canAddSL = false
    }

    val calendar = Calendar.getInstance()
    val options = toDoRepository.getTusk(LoginData.userId)
    var expanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val formatTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    var colorPicker by remember { mutableStateOf(false) }
    var subTaskName by remember { mutableStateOf("") }

    val defaultDate = "1970-01-01"
    val defaultTimeFE = "$defaultDate ${if(calendar[Calendar.HOUR_OF_DAY]<10){"0${calendar[Calendar.HOUR_OF_DAY]}"}else{calendar[Calendar.HOUR_OF_DAY]}}:00"
    val defaultTimeTE = "$defaultDate ${if(calendar[Calendar.HOUR_OF_DAY]<10){"0${calendar[Calendar.HOUR_OF_DAY]+1}"}else{calendar[Calendar.HOUR_OF_DAY]+1}}:00"
    val defaultDateE = LocalDate.now().format(formatDate)

    var event by remember {
        mutableStateOf(
            ev?:Event(
                0,
                "",
                "",
                options[0].id,
                null,
                null,
                null,
                null,
                isTask,
                false,
                "#2471a3",
                null,
                listOf()
            )) }
    var wantDate by remember { mutableStateOf(event.DateStart!=null) }
    var wantTime by remember { mutableStateOf(event.TimeStart!=null) }
    if (!event.Type){
        wantDate = true
    }

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

                    //Name input
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

                    //Category chooser, Task or Event, color chooser
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp), verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(modifier = Modifier
                            .clickable { expanded = true }
                            .padding(start = 20.dp)) {
                            options.find { it.id == event.Category }?.let {
                                Text(
                                    text = it.name,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
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
                                        text = { Text(selectionOption.name) },
                                        onClick = {
                                            event = event.copy(Category = selectionOption.id)
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

                    //Description input
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

                    //Checkboxes for date want
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Date:",color = MaterialTheme.colorScheme.onBackground)
                        Checkbox(checked = wantDate, onCheckedChange = {
                            wantDate = !wantDate
                            event = if(event.DateStart == null){
                                event.copy(DateStart = defaultDateE, DateEnd = defaultDateE)
                            }else{
                                event.copy(DateStart = null, DateEnd = null,TimeStart = null, TimeEnd = null)
                            }
                            if(wantTime && wantDate){
                                event = event.copy(TimeStart = defaultTimeFE, TimeEnd = defaultTimeTE)
                            }
                        }, enabled = event.Type)
                        if (wantDate){
                            Spacer(modifier = Modifier.weight(1f))
                            Text(text = "Time:",color = MaterialTheme.colorScheme.onBackground)
                            Checkbox(checked = wantTime, onCheckedChange = {
                                wantTime = !wantTime
                                event = if(event.TimeStart == null){
                                    event.copy(TimeStart = defaultTimeFE, TimeEnd = defaultTimeTE)
                                }else{
                                    event.copy(TimeStart = null, TimeEnd = null)
                                }
                            })
                        }
                    }

                    //Date Choosers
                    if (wantDate) {
                        if (event.DateStart==null){
                            event = event.copy(DateStart = defaultDateE, DateEnd = defaultDateE)
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            //Date choosers
                            Column {
                                val dateF = LocalDate.parse(event.DateStart, formatDate)
                                val fDatePickerDialog = DatePickerDialog(
                                    LocalContext.current,
                                    { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                                        event = event.copy(
                                            DateStart = "$mYear-${if (mMonth + 1 < 10) { "0${mMonth + 1}" } else { mMonth + 1 }}-${if (mDayOfMonth < 10) { "0${mDayOfMonth}" } else { mDayOfMonth }}")
                                        if (LocalDate.parse(
                                                event.DateEnd,
                                                formatDate
                                            ) < LocalDate.parse(event.DateStart, formatDate)
                                        ) {
                                            event = event.copy(DateEnd = event.DateStart)
                                        }
                                    }, dateF.year, dateF.monthValue - 1, dateF.dayOfMonth
                                )
                                Box(modifier = Modifier
                                    .padding(5.dp)
                                    .clickable {
                                        fDatePickerDialog.show()
                                    }) {
                                    event.DateStart?.let {
                                        Text(
                                            text = it,
                                            color = MaterialTheme.colorScheme.onBackground
                                        )
                                    }
                                }
                                val dateT = LocalDate.parse(event.DateEnd, formatDate)
                                val tDatePickerDialog = DatePickerDialog(
                                    LocalContext.current,
                                    { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                                        event = event.copy(
                                            DateEnd = "$mYear-${if (mMonth + 1 < 10) { "0${mMonth + 1}" } else { mMonth + 1 }}-${if (mDayOfMonth < 10) { "0${mDayOfMonth}" } else { mDayOfMonth }}") }, dateT.year, dateT.monthValue - 1, dateT.dayOfMonth
                                )
                                Box(modifier = Modifier
                                    .padding(5.dp)
                                    .clickable {
                                        tDatePickerDialog.datePicker.minDate = calendar.timeInMillis
                                        tDatePickerDialog.show()
                                    }) {
                                    event.DateEnd?.let {
                                        Text(
                                            text = it,
                                            color = MaterialTheme.colorScheme.onBackground
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            //Time  choosers
                            if (wantTime) {
                                Column {
                                    val timeF = LocalDateTime.parse(event.TimeStart, formatTime)
                                    val fTimePickerDialog = TimePickerDialog(
                                        LocalContext.current,
                                        { _, mHour: Int, mMinute: Int ->
                                            event = event.copy(
                                                TimeStart = "$defaultDate ${if (mHour < 10) { "0$mHour" } else { mHour }}:${if (mMinute < 10) { "0$mMinute" } else { mMinute }}") }, timeF.hour, timeF.minute, true
                                    )
                                    Box(modifier = Modifier
                                        .padding(5.dp)
                                        .clickable {
                                            fTimePickerDialog.show()
                                        }) {
                                        event.TimeStart?.let {
                                            Text(
                                                text = it.takeLast(5),
                                                color = MaterialTheme.colorScheme.onBackground
                                            )
                                        }
                                    }
                                    val timeT = LocalDateTime.parse(event.TimeEnd, formatTime)
                                    val tTimePickerDialog = TimePickerDialog(
                                        LocalContext.current,
                                        { _, mHour: Int, mMinute: Int ->
                                            event = event.copy(
                                                TimeEnd = "$defaultDate ${if (mHour < 10) { "0$mHour" } else { mHour }}:${if (mMinute < 10) { "0$mMinute" } else { mMinute }}") }, timeT.hour, timeT.minute, true
                                    )

                                    val minDate = LocalDate.parse(event.DateStart, formatDate)
                                    calendar.set(
                                        minDate.year,
                                        minDate.monthValue - 1,
                                        minDate.dayOfMonth
                                    )
                                    Box(modifier = Modifier
                                        .padding(5.dp)
                                        .clickable {
                                            tTimePickerDialog.show()
                                        }) {
                                        event.TimeEnd?.let {
                                            Text(
                                                text = it.takeLast(5),
                                                color = MaterialTheme.colorScheme.onBackground
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    //SubTusk Input
                    if (event.Type){
                        Column {
                            Row {
                                OutlinedTextField(value = subTaskName, onValueChange = {subTaskName = it})
                                Button(onClick = {
                                    subList.add(subTaskName)
                                    subTaskName = ""
                                }) {
                                    Text(text = "Add")
                                }
                            }
                            LazyColumn{
                                items(subList){
                                    Text(text = it, color = MaterialTheme.colorScheme.onBackground)
                                }
                            }
                        }
                    }
                }

                //Color Chooser shower
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
            var canSave = true
            if (event.DateStart != null&&event.TimeStart != null){
                canSave = LocalDate.parse(event.DateEnd,formatDate)!=LocalDate.parse(event.DateStart,formatDate)||LocalDate.parse(event.DateEnd,formatDate)==LocalDate.parse(event.DateStart,formatDate)&&LocalDateTime.parse(event.TimeEnd,formatTime)>LocalDateTime.parse(event.TimeStart,formatTime)
            }

            if (canSave) {
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
                                calendarRepository.addEvent(event,subList.toList(),LoginData.userId)
                            } else {
                                navController.navigate(
                                    if (isTask) {
                                        Destinations.TaskList
                                    } else {
                                        Destinations.Schedule
                                    }
                                )
                                calendarRepository.updateEvent(event, subList.toList())
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