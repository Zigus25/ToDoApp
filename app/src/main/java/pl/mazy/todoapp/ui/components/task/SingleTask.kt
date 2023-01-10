@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package pl.mazy.todoapp.ui.components.task

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.mazy.todoapp.Event
import pl.mazy.todoapp.logic.navigation.Destinations
import pl.mazy.todoapp.logic.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleTask(
    navController: NavController<Destinations>,
    ev: Event,
    check:() -> Unit
){
    Card(
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
        modifier = Modifier.padding(10.dp).clickable { navController.navigate(Destinations.EventAdd(ev,ev.Type)) }
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text = ev.Name,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                modifier = Modifier.weight(1f).padding(start = 10.dp),
                overflow = TextOverflow.Ellipsis,
                style = if(ev.Checked){
                    TextStyle(textDecoration = TextDecoration.LineThrough)
                }else{
                    TextStyle(textDecoration = TextDecoration.None)
                }
                )
            Checkbox(checked = ev.Checked, onCheckedChange = {
                check()
            })
        }
    }
}