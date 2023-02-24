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
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import pl.mazy.todoapp.logic.data.LoginData
import pl.mazy.todoapp.logic.navigation.Destinations
import pl.mazy.todoapp.logic.data.repos.ToDoRepository
import pl.mazy.todoapp.logic.navigation.NavController
import pl.mazy.todoapp.ui.components.task.*

@Composable
fun TaskList(
    navController: NavController<Destinations>
) {
    var selAdd = false
    val toDoRepository: ToDoRepository by localDI().instance()
    var titles = toDoRepository.getTusk(LoginData.userId)
    var addingGroup by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    if (titles.isEmpty()) {
        toDoRepository.addCategory("Main",LoginData.userId)
    }

    var category by remember { mutableStateOf(titles[0]) }

    val todos by toDoRepository.getToDos(category.id).collectAsState(initial = listOf())
    fun refreshTitle() = scope.launch {
        titles = toDoRepository.getTusk(LoginData.userId)
    }

    LaunchedEffect(category) {
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
                    text = { Text(text = title.name, maxLines = 1) })
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
                items(todos) { ev ->
                    Task(navController, ev){
                        toDoRepository.changeCheck(it)
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
                            toDoRepository.deleteGroup(category.id)
                            titles = toDoRepository.getTusk(LoginData.userId)
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