package pl.mazy.todoapp.ui.components.task

import android.graphics.Color.parseColor
import android.util.Log
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
import pl.mazy.todoapp.data.model.Event
import pl.mazy.todoapp.navigation.Destinations
import pl.mazy.todoapp.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Task(
    navController: NavController<Destinations>,
    event: Event,
    check:(event: Event) -> Unit
){
    Card(
        border = if (event.mainTask_id==null) {
            BorderStroke(1.dp, Color(parseColor(event.color)))
        }else{
            BorderStroke(0.dp,MaterialTheme.colorScheme.background)
        },
        modifier = Modifier
            .padding(if (event.mainTask_id==null) {10.dp}else{0.dp})
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .clickable { navController.navigate(Destinations.EventAdd(event, event.type)) }
                    .padding(
                        start = if (event.mainTask_id != null) {
                            30.dp
                        } else {
                            5.dp
                        }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (event.mainTask_id != null) {
                    Icon(
                        Icons.Filled.RadioButtonChecked,
                        tint = if (!event.checked) {
                            MaterialTheme.colorScheme.onBackground
                        } else {
                            Color.Red
                        },
                        contentDescription = "",
                    )
                }
                Text(
                    text = event.name,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp),
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        textDecoration = if (event.checked) {
                            TextDecoration.LineThrough
                        } else {
                            TextDecoration.None
                        }
                    )
                )
                Checkbox(checked = event.checked, onCheckedChange = {
                    check(event)
                })
            }
            if (event.subList.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp)
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    event.subList.forEach { subEvent ->
                        Task(navController = navController, event = subEvent, check = {
                            check(it)
                            Log.i("asd", subEvent.id.toString())
                        })
                    }
                }
            }
        }
    }
}