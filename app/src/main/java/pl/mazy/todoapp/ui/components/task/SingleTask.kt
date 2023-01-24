package pl.mazy.todoapp.ui.components.task

import android.graphics.Color.parseColor
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
import pl.mazy.todoapp.logic.data.Event
import pl.mazy.todoapp.logic.navigation.Destinations
import pl.mazy.todoapp.logic.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Task(
    navController: NavController<Destinations>,
    event: Event,
    check:(event: Event) -> Unit
){
    Card(
        border = BorderStroke(1.dp, Color(parseColor(event.Color))),
        modifier = Modifier
            .padding(10.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        SingleTask(navController = navController, event = event) {
            check(event.copy(Checked = !event.Checked))
        }
        Column(modifier = Modifier
            .fillMaxWidth()) {
            event.SubList.forEach{
                SingleTask(navController = navController, event = it) {
                    check(it.copy(Checked = !it.Checked))
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
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .clickable { navController.navigate(Destinations.EventAdd(event, event.Type)) }
            .padding(
                start = if (event.MainTaskID != null) {
                    30.dp
                } else {
                    5.dp
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ){
        if (event.MainTaskID != null){
            Icon(
                Icons.Filled.RadioButtonChecked,
                tint = if (!event.Checked){MaterialTheme.colorScheme.onBackground}else{ Color.Red},
                contentDescription = "",
            )
        }
        Text(text = event.Name,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp),
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(textDecoration = if(event.Checked){
                TextDecoration.LineThrough
            }else{
                TextDecoration.None
            })
        )
        Checkbox(checked = event.Checked, onCheckedChange = {
            check()
        })
    }
}