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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import pl.mazy.todoapp.data.LoginData
import pl.mazy.todoapp.data.interfaces.CalendarInter
import pl.mazy.todoapp.data.interfaces.TasksInter
import pl.mazy.todoapp.data.local.CalendarRepoLocal
import pl.mazy.todoapp.data.local.TasksRepoLocal
import pl.mazy.todoapp.data.model.Category
import pl.mazy.todoapp.data.model.Event
import pl.mazy.todoapp.data.remote.repos.CalendarRepo
import pl.mazy.todoapp.data.remote.repos.TasksRepo
import pl.mazy.todoapp.navigation.Destinations
import pl.mazy.todoapp.navigation.NavController
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventAddEdit(navController: NavController<Destinations>, ev: Event?, isTask:Boolean, cId:Int?) {

    val taskRepo: TasksInter = if (LoginData.token==""){
        val taR: TasksRepoLocal by localDI().instance()
        taR
    }else{
        val taR: TasksRepo by localDI().instance()
        taR
    }
    val calRepo: CalendarInter = if (LoginData.token==""){
        val caR: CalendarRepoLocal by localDI().instance()
        caR
    }else{
        val caR: CalendarRepo by localDI().instance()
        caR
    }
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    var expanded by remember { mutableStateOf(false) }
    var colorPicker by remember { mutableStateOf(false) }

    val subList = remember { mutableListOf<String>() }
    var options:List<Category> by remember { mutableStateOf(listOf()) }
    var subTaskName by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()
    val formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val formatTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    val defaultDate = "1970-01-01"
    val defaultTimeFE = "$defaultDate ${if(calendar[Calendar.HOUR_OF_DAY]<10){"0${calendar[Calendar.HOUR_OF_DAY]}"}else{calendar[Calendar.HOUR_OF_DAY]}}:00"
    val defaultTimeTE = "$defaultDate ${if(calendar[Calendar.HOUR_OF_DAY]<9){"0${calendar[Calendar.HOUR_OF_DAY]+1}"}else{calendar[Calendar.HOUR_OF_DAY]+1}}:00"
    val defaultDateE = LocalDate.now().format(formatDate)

    var event by remember {
        mutableStateOf(
            ev?: Event(
                id = null,
                owner_id = null,
                name = "",
                description = "",
                category_id = cId ?: 0,
                dateEnd = null,
                dateStart = null,
                timeEnd = null,
                timeStart = null,
                type = isTask,
                checked = false,
                color = "#2471a3",
                mainTask_id = null,
                subList = listOf()
            )
        ) }
    var wantDate by remember { mutableStateOf(event.dateStart!=null) }
    var wantTime by remember { mutableStateOf(event.timeEnd!=null) }
    if (!event.type){
        wantDate = true
    }
    fun subRef(){
        if (subList.isEmpty()){
            ev?.subList?.forEach {
                subList.add(it.name)
            }
        }
    }
    LaunchedEffect(options){
        scope.launch {
            options = taskRepo.getCategory()
        }
        subRef()
        if (options.isNotEmpty()&&event.category_id == 0){
            event = event.copy(category_id = options[0].id)
        }
        val ca = options.find { it.id == event.category_id }
        if (ca == null) {
            val cid = options.find { it.shareId == event.category_id }?.id
            if (cid != null) {
                event = event.copy(category_id =cid)
            }
        }
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
                        value = event.name,
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onBackground,
                        ),
                        onValueChange = {
                            event = event.copy(name = it)
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
                            .clickable { if (event.mainTask_id == null) expanded = true }
                            .padding(start = 20.dp)) {
                            options.find { it.id == event.category_id }?.let {
                                Text(
                                    text = it.name,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                            if(event.mainTask_id==null) {
                                Icon(
                                    imageVector = Icons.Default.ExpandMore,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                options.forEach { selectionOption ->
                                    DropdownMenuItem(
                                        text = { Text(selectionOption.name) },
                                        onClick = {
                                            event = event.copy(category_id = selectionOption.id)
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
                                checked = event.type,
                                onCheckedChange = {
                                    if(event.subList.isEmpty()&&event.mainTask_id==null) {
                                        event = event.copy(type = !event.type)
                                    }
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
                                                    event.color
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
                        value = event.description.toString(),
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onBackground,
                        ),
                        onValueChange = {
                            event = event.copy(description = it)
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
                            event = if(event.dateStart == null){
                                event.copy(dateStart = defaultDateE, dateEnd = defaultDateE)
                            }else{
                                event.copy(dateStart = null, dateEnd = null, timeStart = null, timeEnd = null)
                            }
                            if(wantTime && wantDate){
                                event = event.copy(timeStart = defaultTimeFE, timeEnd = defaultTimeTE)
                            }
                        }, enabled = event.type)
                        if (wantDate){
                            Spacer(modifier = Modifier.weight(1f))
                            Text(text = "Time:",color = MaterialTheme.colorScheme.onBackground)
                            Checkbox(checked = wantTime, onCheckedChange = {
                                wantTime = !wantTime
                                event = if(event.timeStart == null){
                                    event.copy(timeStart = defaultTimeFE, timeEnd = defaultTimeTE)
                                }else{
                                    event.copy(timeStart = null, timeEnd = null)
                                }
                            })
                        }
                    }

                    //Date Choosers
                    if (wantDate) {
                        if (event.dateStart==null){
                            event = event.copy(dateStart = defaultDateE, dateEnd = defaultDateE)
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            //Date choosers
                            Column {
                                val dateF = LocalDate.parse(event.dateStart, formatDate)
                                val fDatePickerDialog = DatePickerDialog(
                                    LocalContext.current,
                                    { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                                        event = event.copy(
                                            dateStart = "$mYear-${if (mMonth + 1 < 10) { "0${mMonth + 1}" } else { mMonth + 1 }}-${if (mDayOfMonth < 10) { "0${mDayOfMonth}" } else { mDayOfMonth }}")
                                        if (LocalDate.parse(
                                                event.dateEnd,
                                                formatDate
                                            ) < LocalDate.parse(event.dateStart, formatDate)
                                        ) {
                                            event = event.copy(dateEnd = event.dateStart)
                                        }
                                    }, dateF.year, dateF.monthValue - 1, dateF.dayOfMonth
                                )
                                Box(modifier = Modifier
                                    .padding(5.dp)
                                    .clickable {
                                        fDatePickerDialog.show()
                                    }) {
                                    event.dateStart?.let {
                                        Text(
                                            text = it,
                                            color = MaterialTheme.colorScheme.onBackground
                                        )
                                    }
                                }
                                val dateT = LocalDate.parse(event.dateEnd, formatDate)
                                val tDatePickerDialog = DatePickerDialog(
                                    LocalContext.current,
                                    { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                                        event = event.copy(
                                            dateEnd = "$mYear-${if (mMonth + 1 < 10) { "0${mMonth + 1}" } else { mMonth + 1 }}-${if (mDayOfMonth < 10) { "0${mDayOfMonth}" } else { mDayOfMonth }}") }, dateT.year, dateT.monthValue - 1, dateT.dayOfMonth
                                )
                                Box(modifier = Modifier
                                    .padding(5.dp)
                                    .clickable {
                                        tDatePickerDialog.datePicker.minDate = calendar.timeInMillis
                                        tDatePickerDialog.show()
                                    }) {
                                    event.dateEnd?.let {
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
                                    val timeF = LocalDateTime.parse(event.timeStart, formatTime)
                                    val fTimePickerDialog = TimePickerDialog(
                                        LocalContext.current,
                                        { _, mHour: Int, mMinute: Int ->
                                            event = event.copy(
                                                timeStart = "$defaultDate ${if (mHour < 10) { "0$mHour" } else { mHour }}:${if (mMinute < 10) { "0$mMinute" } else { mMinute }}") }, timeF.hour, timeF.minute, true
                                    )
                                    Box(modifier = Modifier
                                        .padding(5.dp)
                                        .clickable {
                                            fTimePickerDialog.show()
                                        }) {
                                        event.timeStart?.let {
                                            Text(
                                                text = it.takeLast(5),
                                                color = MaterialTheme.colorScheme.onBackground
                                            )
                                        }
                                    }
                                    val timeT = LocalDateTime.parse(event.timeEnd, formatTime)
                                    val tTimePickerDialog = TimePickerDialog(
                                        LocalContext.current,
                                        { _, mHour: Int, mMinute: Int ->
                                            event = event.copy(
                                                timeEnd = "$defaultDate ${if (mHour < 10) { "0$mHour" } else { mHour }}:${if (mMinute < 10) { "0$mMinute" } else { mMinute }}") }, timeT.hour, timeT.minute, true
                                    )

                                    val minDate = LocalDate.parse(event.dateStart, formatDate)
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
                                        event.timeEnd?.let {
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
                    if (event.type){
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
                    PickAColor({ colorPicker = false }, { event = event.copy(color = it) })
                }
            }
        }


        BottomAppBar {
            if (ev != null) {
                IconButton(onClick = {
                    val cat = options.find { it.shareId == ev.category_id }?.id
                    navController.navigate(if (isTask){
                        Destinations.TaskList(cat?:ev.category_id)}else{
                        Destinations.Schedule})
                    scope.launch { calRepo.delEvent(ev) }
                }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Menu Icon")
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            var canSave = true
            if (event.dateStart != null&&event.timeStart != null){
                canSave = LocalDate.parse(event.dateEnd,formatDate)!=LocalDate.parse(event.dateStart,formatDate)||LocalDate.parse(event.dateEnd,formatDate)==LocalDate.parse(event.dateStart,formatDate)&&LocalDateTime.parse(event.timeEnd,formatTime)>LocalDateTime.parse(event.timeStart,formatTime)
            }

            if (canSave) {
                Box(modifier = Modifier.padding(10.dp)) {
                    SmallFloatingActionButton(
                        onClick = {
                            val cat = options.find { it.id == event.category_id }
                            if (cat?.shareId != null){
                                event = event.copy(category_id = cat.shareId)
                            }
                            if (cat!=null) {
                                if (ev == null) {
                                    scope.launch {
                                        calRepo.addEvent(event, subList.toList())
                                        navController.navigate(
                                            if (isTask) {
                                                Destinations.TaskList(cat.id)
                                            } else {
                                                Destinations.Schedule
                                            }
                                        )
                                    }
                                } else {
                                    scope.launch {
                                        calRepo.updateEvent(event, subList.toList())
                                        navController.navigate(
                                            if (isTask) {
                                                Destinations.TaskList(cat.id)
                                            } else {
                                                Destinations.Schedule
                                            }
                                        )
                                    }
                                }
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