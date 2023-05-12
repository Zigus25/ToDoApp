package pl.mazy.todoapp.ui.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import pl.mazy.todoapp.navigation.Destinations
import pl.mazy.todoapp.data.LoginData
import pl.mazy.todoapp.data.interfaces.NotesInter
import pl.mazy.todoapp.data.local.NotesRepoLocal
import pl.mazy.todoapp.data.model.Note
import pl.mazy.todoapp.data.remote.repos.NotesRepo
import pl.mazy.todoapp.navigation.NavController
import pl.mazy.todoapp.ui.components.note.*

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NoteList(
    navController: NavController<Destinations>,
){
    val noteRepo:NotesInter = if (LoginData.token==""){
        val noR:NotesRepoLocal by localDI().instance()
        noR
    }else{
        val noR: NotesRepo by localDI().instance()
        noR
    }
    var notes: List<Note> by remember { mutableStateOf(listOf()) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(notes,LoginData.login) {
        scope.launch {
            notes = noteRepo.getNotes()
        }
    }
    Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate(Destinations.NoteDetails(null)) },
                    modifier = Modifier
                        .height(50.dp)
                        .width(50.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                    )
                }
            },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.BottomCenter) {
                LazyVerticalStaggeredGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    columns = StaggeredGridCells.Adaptive(150.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(notes) { note ->
                        SingleNote(note, navController)
                    }
                }
            }
        }
    }
}