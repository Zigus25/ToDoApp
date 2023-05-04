package pl.mazy.todoapp.ui.components.note

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
import kotlinx.coroutines.launch
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import pl.mazy.todoapp.data.LoginData
import pl.mazy.todoapp.data.interfaces.NotesInter
import pl.mazy.todoapp.data.local.NotesRepoLocal
import pl.mazy.todoapp.navigation.Destinations
import pl.mazy.todoapp.data.model.Note
import pl.mazy.todoapp.data.remote.repos.NotesRepo
import pl.mazy.todoapp.navigation.NavController

@Composable
fun NoteAdding(
    navController: NavController<Destinations>,
    noteP: Note?
){
    val focusManager = LocalFocusManager.current
    val noteRepo: NotesInter = if (LoginData.token==""){
        val noR: NotesRepoLocal by localDI().instance()
        noR
    }else{
        val noR: NotesRepo by localDI().instance()
        noR
    }

    val scope = rememberCoroutineScope()
    var note by remember {
        mutableStateOf(
            noteP?: Note(
                null,
                null,
                "",
                null
            )
        ) }



    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){
        OutlinedTextField(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            value = note.name,
            textStyle= TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
            ),
            onValueChange = { note = note.copy(name = it) },
            keyboardActions = KeyboardActions(onDone = { focusManager.moveFocus(
                FocusDirection.Down) }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            label = { Text("Name") }
        )

        note.description?.let {
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(5.dp),
                value = it,
                textStyle= TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                ),
                onValueChange = { note = note.copy(description = it) },
                label = { Text("Description") }
            )
        }

        BottomAppBar {
            if (noteP != null) {
                if (noteP.id != null){
                    IconButton(onClick = {
                        navController.navigate(Destinations.Notes)
                        scope.launch {
                            noteRepo.deleteNote(noteP.id)
                        }
                    }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Menu Icon")
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(modifier = Modifier.padding(10.dp)) {
                SmallFloatingActionButton(
                    onClick = {
                        scope.launch {
                            if (noteP == null) {
                                navController.navigate(Destinations.Notes)

                                noteRepo.addNote(note)
                            } else {
                                navController.navigate(Destinations.Notes)
                                noteRepo.updateNote(note)
                            }
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