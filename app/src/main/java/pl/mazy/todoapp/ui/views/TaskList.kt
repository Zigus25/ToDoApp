package pl.mazy.todoapp.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import pl.mazy.todoapp.Event
import pl.mazy.todoapp.logic.navigation.Destinations
import pl.mazy.todoapp.logic.data.ToDoRepository
import pl.mazy.todoapp.logic.navigation.NavController
import pl.mazy.todoapp.ui.components.task.*

@Composable
fun TaskList(
    navController: NavController<Destinations>,
) {
    var selAdd = false
    val toDoRepository: ToDoRepository by localDI().instance()
    var titles = toDoRepository.getTusk()
    var addingGroup by remember { mutableStateOf(false) }
    var category by remember { mutableStateOf(titles[0]) }
    var change by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var todos: Flow<List<Event>>?  by remember { mutableStateOf(toDoRepository.getToDos(category)) }

    if (titles.isEmpty()) {
        toDoRepository.addCategory("Main")
        todos = toDoRepository.getToDos(category)
    }

    fun refreshTitle() = scope.launch {
        titles = toDoRepository.getTusk()
    }

    LaunchedEffect(category,change) {
        refreshTitle()
    }
    Column(modifier = Modifier.fillMaxSize()) {
        val i = titles.indexOf(category)
        ScrollableTabRow(
            selectedTabIndex = i, edgePadding = 15.dp, modifier = Modifier.fillMaxWidth(), divider = {}
        ) {
            titles.forEachIndexed { index, title ->
                Tab(modifier = Modifier.weight(1f),
                    selected = i == index,
                    onClick = { category = title },
                    unselectedContentColor = MaterialTheme.colorScheme.onBackground,
                    text = { Text(text = title, maxLines = 1) })
            }
            Tab(selected = selAdd,
                onClick = {
                    selAdd = true
                    addingGroup = true
                },
                unselectedContentColor = MaterialTheme.colorScheme.onBackground,
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Add new ", maxLines = 1)
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Add Icon",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                }})
        }
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.BottomCenter) {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                if (todos != null) {
                    items(todos) { ev ->
                        Task(navController, ev){
                            change = !change
                        }
                    }
                }
            }
            if (addingGroup) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8F))
                    .blur(8.dp)
                    .clickable { addingGroup = false })
                GroupAdd { addingGroup = false }
            }
            if (!addingGroup) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (titles.size > 1) {
                        IconButton(onClick = {
                            toDoRepository.deleteGroup(category)
                            titles = toDoRepository.getTusk()
                            category = titles[0]
                        }) {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = "Delete Icon",
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Box(modifier = Modifier.padding(15.dp)) {
                        SmallFloatingActionButton(
                            onClick = { navController.navigate(Destinations.EventAdd(null, true)) },
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
                }
            }
        }
    }
}