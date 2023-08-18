package pl.mazy.todoapp.ui.components.task

import android.graphics.Color.parseColor
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    hidden: List<Int>,
    check:(event: Event,opp:Boolean) -> Unit,
    hide:(idE: Int,ope:Boolean) -> Unit,
    clicked:(e:Event)->Unit
){
    var expend by remember { mutableStateOf(!hidden.contains(event.id)) }
    var expendUA by remember { mutableStateOf(false )}
    Card(
        border = if (event.mainTask_id==null) {
            BorderStroke(2.dp, Color(parseColor(event.color)).copy(alpha = 0.6F))
        }else{
            BorderStroke(0.dp,MaterialTheme.colorScheme.background)
        },
        modifier = Modifier
            .padding(
                if (event.mainTask_id == null) {
                    10.dp
                } else {
                    0.dp
                }
            )
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
                    .clickable {
                        clicked(event)
                    }
                    .padding(
                        start = if (event.mainTask_id != null) {
                            30.dp
                        } else {
                            5.dp
                        }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = event.name,
                    fontSize = 18.sp,
                    color = if(!event.checked){MaterialTheme.colorScheme.onBackground}else{MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4F)},
                    maxLines = 2,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp),
                    overflow = TextOverflow.Ellipsis
                )
                if (event.subList.isNotEmpty()) {
                    IconButton(onClick = {
                        if (expend){
                            event.id?.let { hide(it,true) }
                        }else{
                            event.id?.let { hide(it,false) }
                        }
                        expend = !expend
                    }) {
                        Icon(
                            imageVector = if (expend) {
                                Icons.Filled.ExpandMore
                            } else {
                                Icons.Filled.ExpandLess
                            },
                            contentDescription = "expand",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
                Checkbox(checked = event.checked, onCheckedChange = {
                    check(event,true)
                })
            }
            if (event.subList.isNotEmpty()&&expend) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp)
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    event.subList.forEach { subEvent ->
                        Task(navController = navController, event = subEvent,hidden = hidden, check = { eve,opp->
                            check(eve,true)
                        },hide = {idE, ope ->
                            hide(idE,ope)
                        },clicked = {clicked(it)})
                    }
                }
            }
            if (event.mainTask_id == null&&expend){
                Column (horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    IconButton(onClick = {
                        expendUA = !expendUA
                    }) {
                        Icon(
                            imageVector = if (expendUA) {
                                Icons.Filled.ExpandMore
                            } else {
                                Icons.Filled.ExpandLess
                            },
                            contentDescription = "expand",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    if (expendUA) {
                        Text(text = "Unmark ALL", modifier = Modifier.clickable {
                            check(event,false)
                        }.padding(bottom = 10.dp), color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}