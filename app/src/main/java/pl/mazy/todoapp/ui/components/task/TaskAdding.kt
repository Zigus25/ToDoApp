@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package pl.mazy.todoapp.ui.components.task

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import pl.mazy.todoapp.logic.data.ToDoRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskAdding(
    closeAdder: () -> Unit = {},
    categoryS:String
){
    val focusManager = LocalFocusManager.current
    val toDoRepository: ToDoRepository by localDI().instance()
    var text by remember { mutableStateOf("") }
    val options = toDoRepository.getTusk()
    var expanded by remember { mutableStateOf(false) }
    var category:String = categoryS

    Card(
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
        modifier = Modifier.padding(10.dp).fillMaxWidth()
    ) {
        Column(
            Modifier
                .fillMaxWidth()

        ) {
            Row{
                OutlinedTextField(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                    value = text,
                    textStyle= TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                    ),
                    onValueChange = { text = it },
                    keyboardActions = KeyboardActions(onDone = { focusManager.moveFocus(
                        FocusDirection.Down) }),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    label = { Text("New Task") },
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null,
                )
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                )

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


                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = {
                    toDoRepository.addToDo(text,category)
                    closeAdder()},
                    modifier = Modifier.padding(end = 10.dp)) {
                    Text(text = "Save")
                }
            }
        }
    }
}