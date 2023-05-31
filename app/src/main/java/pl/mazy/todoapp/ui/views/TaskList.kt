package pl.mazy.todoapp.ui.views

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import pl.mazy.todoapp.data.LoginData
import pl.mazy.todoapp.data.interfaces.TasksInter
import pl.mazy.todoapp.data.local.TasksRepoLocal
import pl.mazy.todoapp.navigation.Destinations
import pl.mazy.todoapp.data.model.Category
import pl.mazy.todoapp.data.model.Event
import pl.mazy.todoapp.data.remote.repos.TasksRepo
import pl.mazy.todoapp.navigation.NavController
import pl.mazy.todoapp.ui.components.task.*

@Composable
fun TaskList(
    navController: NavController<Destinations>
) {
    var i by remember {
        mutableStateOf(0)
    }
    val taskRepo: TasksInter = if (LoginData.token==""){
        val taR: TasksRepoLocal by localDI().instance()
        taR
    }else{
        val taR: TasksRepo by localDI().instance()
        taR
    }
    var checked by remember { mutableStateOf(0) }
    var titles:List<Category> by remember { mutableStateOf(listOf()) }
    var addingGroup by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    var category:Category by remember { mutableStateOf(Category(-1,"",0,null)) }

    var todos :List<Event> by remember {
        mutableStateOf(listOf())
    }
    fun refreshTitle() = scope.launch {
        titles = taskRepo.getCategory()
        if (titles.isEmpty()){
            titles = listOf(Category(-1,"",0,null))
        }else{
            category = titles[i]
        }
    }

    fun refreshEvents(cat:Category) = scope.launch {
        todos = if (cat.shareId==null) {
            taskRepo.getTusks(cat.id)
        }else{
            taskRepo.getTusks(cat.shareId!!)
        }
    }

    LaunchedEffect(category,titles,checked,LoginData.login) {
        scope.launch { titles = taskRepo.getCategory() }
        refreshTitle()

        scope.launch {
            if ((titles.isEmpty()||titles[0].id==-1)&&LoginData.token == "") {
                taskRepo.addCategory("Main")
                refreshTitle()
            }
        }
        if(category.id!=-1) {
            refreshEvents(category)
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        ScrollableTabRow(
            selectedTabIndex = if(addingGroup){titles.size}else{i}, edgePadding = 15.dp, modifier = Modifier.fillMaxWidth(), divider = {}
        ) {
            titles.forEachIndexed { index, title ->
                Tab(modifier = Modifier.weight(1f),
                    selected = !addingGroup&&i == index,
                    onClick = {
                        category = title
                        addingGroup = false
                        i = index
                    },
                    unselectedContentColor = MaterialTheme.colorScheme.onBackground,
                    text = { Text(text = title.name, maxLines = 1) })
            }
            Tab(selected = addingGroup,
                onClick = {
                    addingGroup = true
                },
                unselectedContentColor = MaterialTheme.colorScheme.onBackground,
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Group ", maxLines = 1)
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Add Icon",
                            tint = if(!addingGroup){MaterialTheme.colorScheme.onBackground}else{MaterialTheme.colorScheme.primary}
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
                        scope.launch {
                            taskRepo.toggle(it)
                            checked++
                        }
                    }
                }
            }
            Column {
                AnimatedVisibility(visible = addingGroup) {
                    Column(
                        Modifier
                            .clickable { addingGroup = false }
                            .fillMaxSize(1f)
                            .background(Brush.verticalGradient( listOf( MaterialTheme.colorScheme.primary.copy(alpha = 0F), MaterialTheme.colorScheme.primary.copy(alpha = 0.18F))))
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        GroupAdd {
                            addingGroup = false
                            titles = listOf()
                        }
                    }
                }
            }
            Column {
                AnimatedVisibility(visible = !addingGroup) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (titles.size > 1) {
                            IconButton(onClick = {
                                scope.launch {
                                    taskRepo.delCategory(category.id)
                                    titles = taskRepo.getCategory()
                                    category = titles[0]
                                    i = 0
                                }
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
                                onClick = {
                                    navController.navigate(
                                        Destinations.EventAdd(
                                            null,
                                            true,
                                            category.id
                                        )
                                    )
                                },
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
}