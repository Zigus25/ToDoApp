package pl.mazy.todoapp.ui.components.task

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import pl.mazy.todoapp.Event
import pl.mazy.todoapp.logic.data.ToDoRepository
import pl.mazy.todoapp.logic.navigation.Destinations
import pl.mazy.todoapp.logic.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Task(
    navController: NavController<Destinations>,
    ev: Event
){
    val toDoRepository: ToDoRepository by localDI().instance()
    Card(
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
        modifier = Modifier
            .padding(10.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        SingleTask(navController = navController, event = ev) {
            toDoRepository.updateState(ev)
        }
        Column(modifier = Modifier
            .fillMaxWidth()) {
            toDoRepository.selSubListByID(ev.id).forEach{
                SingleTask(navController = navController, event = it) {
                    toDoRepository.updateState(it)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleTask(
    navController: NavController<Destinations>,
    event: Event,
    check:() -> Unit
){
    var ev by remember { mutableStateOf(event) }
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .clickable { navController.navigate(Destinations.EventAdd(ev, ev.Type)) }
            .padding(start = if (ev.MainTaskID != null){30.dp}else{5.dp}),
        verticalAlignment = Alignment.CenterVertically
    ){
        if (ev.MainTaskID != null){
            Icon(
                Icons.Filled.RadioButtonChecked,
                tint = if (!ev.Checked){MaterialTheme.colorScheme.onBackground}else{ Color.Red},
                contentDescription = "",
            )
        }
        Text(text = ev.Name,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp),
            overflow = TextOverflow.Ellipsis,
            style = if(ev.Checked){
                TextStyle(textDecoration = TextDecoration.LineThrough)
            }else{
                TextStyle(textDecoration = TextDecoration.None)
            }
        )
        Checkbox(checked = ev.Checked, onCheckedChange = {
            check()
            ev = ev.copy(Checked = !ev.Checked)
        })
    }
}