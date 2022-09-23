package pl.mazy.todoapp.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
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
import pl.mazy.todoapp.Destinations
import pl.mazy.todoapp.data.ToDoRepository
import pl.mazy.todoapp.logic.dataClass.Task
import pl.mazy.todoapp.navigation.NavController
import pl.mazy.todoapp.ui.components.SingleTask
import pl.mazy.todoapp.ui.components.TaskAdding

@Composable
fun TaskList(
    navController: NavController<Destinations>,
){
    var selAdd = false
    val toDoRepository: ToDoRepository by localDI().instance()
    var titles = toDoRepository.getTusk()
    var adding by remember { mutableStateOf(false) }
    var change by remember { mutableStateOf(false) }
    var category by remember { mutableStateOf("Main") }
    val scope = rememberCoroutineScope()
    var todos: List<Task>? by remember { mutableStateOf(null) }

    if (titles.isEmpty()){
        toDoRepository.addCategory("Main")
    }

    fun refreshTusk() = scope.launch {
        titles = toDoRepository.getTusk()
    }

    fun loadTodos() = scope.launch {
        todos = toDoRepository.getToDos(category)
    }

    LaunchedEffect (adding,category,change) {
        refreshTusk()
        loadTodos()
    }

    Column(modifier = Modifier.fillMaxSize()){
        var i = titles.indexOf(category)
        TabRow(selectedTabIndex = i) {
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
                },
                text = { Text(text = "Add new",maxLines = 1) })
        }
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.BottomCenter){
            LazyColumn (
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ){
                items(todos?: listOf()) { task->
                    SingleTask(navController,task,
                        check = {
                            toDoRepository.updateState(task.name)
                            change = !change
                        }
                    )
                }
            }
            if (adding){
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7F))
                    .blur(8.dp)
                    .clickable { adding = false })
                TaskAdding{adding = false}
            }
        }
        BottomAppBar{
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(Icons.Filled.Menu, contentDescription = "Menu Icon")
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(modifier = Modifier.weight(1f)){
                SmallFloatingActionButton(onClick = { adding = true },
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
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(
                    Icons.Filled.CalendarMonth,
                    contentDescription = "Calendar Icon",
                )
            }
        }
    }

}