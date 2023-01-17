package pl.mazy.todoapp.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
){
    var selAdd = false
    val toDoRepository: ToDoRepository by localDI().instance()
    var titles = toDoRepository.getTusk()
    var addingGroup by remember { mutableStateOf(false) }
    var change by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var todos: List<Event>? by remember { mutableStateOf(null) }

    if (titles.isEmpty()){
        toDoRepository.addCategory("Main")
    }

    var category by remember { mutableStateOf(titles[0]) }

    fun refreshTusk() = scope.launch {
        titles = toDoRepository.getTusk()
    }

    fun loadTodos() = scope.launch {
        todos = toDoRepository.getToDos(category)
    }

    LaunchedEffect (category,change) {
        refreshTusk()
        loadTodos()
    }
    loadTodos()
    Column(modifier = Modifier.fillMaxSize()){
        val i = titles.indexOf(category)
        ScrollableTabRow(selectedTabIndex = i,edgePadding = 0.dp) {
            titles.forEachIndexed{index,title ->
                Tab(
                    selected = i == index,
                    onClick = { category = title },
                    text = { Text(text = title,maxLines = 1) })
            }
            Tab(
                selected = selAdd,
                onClick = {
                    selAdd = true
                    addingGroup = true
                },
                text = { Text(text = "Add new",maxLines = 1) })
        }
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.BottomCenter){
            LazyColumn (
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ){
                if (todos!=null) {
                    items(todos ?: listOf()) { ev ->
                        Task(navController, ev)
                    }
                }
            }
            if (addingGroup){
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7F))
                    .blur(8.dp)
                    .clickable { addingGroup = false })
                GroupAdd {addingGroup = false}
            }
        }
        var expandedD by remember { mutableStateOf(false) }
        BottomAppBar{
            Box{
                IconButton(onClick = { expandedD = true }) {
                    Icon(Icons.Filled.Menu, contentDescription = "Menu Icon")
                }
                DropdownMenu(
                    expanded = expandedD,
                    onDismissRequest = { expandedD = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Delete group") },
                        onClick = {
                            expandedD = false
                            toDoRepository.deleteGroup(category)
                            titles = toDoRepository.getTusk()
                            category = titles[0]
                            change = !change
                        }
                    )

                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(modifier = Modifier.weight(1f)){
                SmallFloatingActionButton(onClick = { navController.navigate(Destinations.EventAdd(null,true)) },
                    modifier = Modifier
                        .height(50.dp)
                        .width(50.dp)) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                    )
                }
            }

            IconButton(onClick = { navController.navigate(Destinations.Notes) }) {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = "Calendar Icon",
                )
            }
            IconButton(onClick = { navController.navigate(Destinations.Schedule) }) {
                Icon(
                    Icons.Filled.CalendarMonth,
                    contentDescription = "Calendar Icon",
                )
            }
        }
    }

}