package pl.mazy.todoapp.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import pl.mazy.todoapp.Destinations
import pl.mazy.todoapp.Notes
import pl.mazy.todoapp.data.NotesRepository
import pl.mazy.todoapp.navigation.NavController
import pl.mazy.todoapp.ui.components.SingleNote
import pl.mazy.todoapp.ui.components.TaskAdding

@Composable
fun NoteList(
    navController: NavController<Destinations>,
){
    val notesRepository: NotesRepository by localDI().instance()
    var adding by remember { mutableStateOf(false) }
    var change by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var notes: List<Notes>? by remember { mutableStateOf(null) }

    if (notes?.isEmpty() == true){
        notesRepository.addNote("Hello","This is sample description")
    }

    fun loadTodos() = scope.launch {
        notes = notesRepository.getNotes()
    }

    LaunchedEffect (adding,change) {
        loadTodos()
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.BottomCenter) {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                columns = GridCells.Adaptive(150.dp)
            ) {
                items(notes ?: listOf()) { note ->
                        SingleNote(note,navController)
                }
            }
            if (adding) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7F))
                    .blur(8.dp)
                    .clickable { adding = false })
                TaskAdding() { adding = false }
            }
        }
        BottomAppBar {
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(Icons.Filled.Menu, contentDescription = "Menu Icon")
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(modifier = Modifier.weight(1f)) {
                SmallFloatingActionButton(
                    onClick = { navController.navigate(Destinations.CreateNote) },
                    modifier = Modifier
                        .height(50.dp)
                        .width(50.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                    )
                }
            }

            IconButton(onClick = { navController.navigate(Destinations.TaskList) }) {
                Icon(
                    Icons.Filled.Checklist,
                    contentDescription = "Calendar Icon",
                )
            }
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(
                    Icons.Filled.CalendarMonth,
                    contentDescription = "Calendar Icon",
                )
            }
        }
    }
}