@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package pl.mazy.todoapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import pl.mazy.todoapp.Destinations
import pl.mazy.todoapp.data.ToDoRepository
import pl.mazy.todoapp.logic.dataClass.Task
import pl.mazy.todoapp.navigation.NavController

@Composable
fun TaskEdit(
    navController: NavController<Destinations>,
    task: Task

){
    val focusManager = LocalFocusManager.current
    val toDoRepository: ToDoRepository by localDI().instance()

    val options = toDoRepository.getTusk()
    var expanded by remember { mutableStateOf(false) }
    var category by remember { mutableStateOf(task.category) }
    val name by remember { mutableStateOf(task.name) }
    val description by remember { mutableStateOf("") }
    val date by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {
        Row{
            Row(modifier = Modifier.clickable{ expanded = true }.padding(10.dp)) {
                Text(
                    text = category,
                    color = MaterialTheme.colorScheme.onBackground)
                Icon(imageVector = Icons.Default.ExpandMore, contentDescription = null)
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                category = selectionOption
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
        Row{
            OutlinedTextField(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                value = name,
                textStyle= TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                ),
                onValueChange = { category = it },
                keyboardActions = KeyboardActions(onDone = { focusManager.moveFocus(
                    FocusDirection.Down) }),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                label = { Text("Task name") },
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        BottomAppBar {
            if (task.name.isNotEmpty()){
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .clickable {
                            toDoRepository.deleteTask(task.name)
                        }
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(modifier = Modifier.padding(10.dp)) {
                SmallFloatingActionButton(
                    onClick = {
                        navController.navigate(Destinations.Notes)
                        toDoRepository.updateTask(name,description,date,category,task.name,task.ID)
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