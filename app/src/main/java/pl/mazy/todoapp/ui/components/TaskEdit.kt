package pl.mazy.todoapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.SaveAs
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEdit(
    navController: NavController<Destinations>,
    task: Task

){
    val focusManager = LocalFocusManager.current
    val toDoRepository: ToDoRepository by localDI().instance()

    val options = toDoRepository.getTusk()
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }
    var text by remember { mutableStateOf(task.category) }
    val name by remember { mutableStateOf(task.name) }
    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {
        Row{
            ExposedDropdownMenuBox(
                modifier = Modifier.width(200.dp),
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
            ) {
                TextField(
                    value = selectedOptionText,
                    onValueChange = { selectedOptionText = it },
                    readOnly = true,
                    label = { Text("Group") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                )
                // filter options based on text field value
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                selectedOptionText = selectionOption
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
                onValueChange = { text = it },
                keyboardActions = KeyboardActions(onDone = { focusManager.moveFocus(
                    FocusDirection.Down) }),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                label = { Text("Task name") },
            )
        }
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
//                        toDoRepository.updateNote(name,text,nameM)
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