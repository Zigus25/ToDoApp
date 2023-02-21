@file:Suppress("OPT_IN_IS_NOT_ENABLED")

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
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import pl.mazy.todoapp.logic.navigation.Destinations
import pl.mazy.todoapp.Notes
import pl.mazy.todoapp.logic.data.repos.NotesRepository
import pl.mazy.todoapp.logic.navigation.NavController
import pl.mazy.todoapp.ui.components.note.*

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NoteList(
    navController: NavController<Destinations>,
){
    val notesRepository: NotesRepository by localDI().instance()
    val notes: List<Notes>? by remember { mutableStateOf(notesRepository.getNotes()) }

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
                    items(notes ?: listOf()) { note ->
                        SingleNote(note, navController)
                    }
                }
            }
        }
    }
}