package pl.mazy.todoapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import pl.mazy.todoapp.data.NotesRepository
import pl.mazy.todoapp.navigation.NavController

@Composable
fun NoteAdding(
    navController: NavController<Destinations>,
    nameM:String = "",
    des:String = ""
){
    val focusManager = LocalFocusManager.current
    val noteRepository: NotesRepository by localDI().instance()
    var text by remember { mutableStateOf(des) }
    var name by remember { mutableStateOf(nameM) }



    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){
        OutlinedTextField(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            value = name,
            textStyle= TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
            ),
            onValueChange = { name = it },
            keyboardActions = KeyboardActions(onDone = { focusManager.moveFocus(
                FocusDirection.Down) }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            label = { Text("Name") }
        )

        OutlinedTextField(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(5.dp),
            value = text,
            textStyle= TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
            ),
            onValueChange = { text = it },
            label = { Text("Description") }
        )

        BottomAppBar {
            if (nameM.isNotEmpty()){
                IconButton(onClick = {
                    navController.navigate(Destinations.Notes)
                    noteRepository.deleteNote(nameM,des)
                }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Menu Icon")
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(modifier = Modifier.padding(10.dp)) {
                SmallFloatingActionButton(
                    onClick = {
                        if(nameM.isEmpty()){
                            navController.navigate(Destinations.Notes)
                            noteRepository.addNote(name,text)
                        }else{
                            navController.navigate(Destinations.Notes)
                            noteRepository.updateNote(name,text,nameM)
                        }
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