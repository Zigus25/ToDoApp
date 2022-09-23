package pl.mazy.todoapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.mazy.todoapp.Destinations
import pl.mazy.todoapp.Tasks
import pl.mazy.todoapp.logic.dataClass.Task
import pl.mazy.todoapp.navigation.NavController
import pl.mazy.todoapp.ui.theme.ToDoAPpTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleTask(
    navController: NavController<Destinations>,
    task: Task,
    check:() -> Unit
){
    ToDoAPpTheme {
        Card(
            border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
            modifier = Modifier.padding(10.dp).clickable { navController.navigate(Destinations.TaskDetails(task)) }
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(text = task.name,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    modifier = Modifier.weight(1f).padding(start = 10.dp),
                    overflow = TextOverflow.Ellipsis,
                    style = if(task.checked){
                        TextStyle(textDecoration = TextDecoration.LineThrough)
                    }else{
                        TextStyle(textDecoration = TextDecoration.None)
                    }
                    )
                Checkbox(checked = task.checked, onCheckedChange = {
                    check()
                })
            }
        }
    }
}