package pl.mazy.todoapp.ui.components.task

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import pl.mazy.todoapp.data.LoginData
import pl.mazy.todoapp.data.interfaces.TasksInter
import pl.mazy.todoapp.data.local.TasksRepoLocal
import pl.mazy.todoapp.data.remote.repos.TasksRepo

@Composable
fun GroupAdd(closeAdder: () -> Unit = {}){
    val focusManager = LocalFocusManager.current
    val taskRepo: TasksInter = if (LoginData.token==""){
        val taR: TasksRepoLocal by localDI().instance()
        taR
    }else{
        val taR: TasksRepo by localDI().instance()
        taR
    }
    var text by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()


    Column(
        Modifier
            .fillMaxWidth()
            .padding(10.dp)
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
                label = { Text("New Group") },
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = {
                scope.launch {
                    taskRepo.addCategory(text)
                    closeAdder()
                }},
                modifier = Modifier.padding(end = 10.dp)) {
                Text(text = "Save")
            }
        }
    }
}