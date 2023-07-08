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
import androidx.compose.ui.graphics.Brush
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
    navController: NavController<Destinations>,
    backCategory: Int
) {
    var i by remember {
        mutableStateOf(0)
    }
    var s by remember {
        mutableStateOf(-1)
    }
    val taskRepo: TasksInter = if (LoginData.token==""){
        val taR: TasksRepoLocal by localDI().instance()
        taR
    }else{
        val taR: TasksRepo by localDI().instance()
        taR
    }
    var logRe by remember { mutableStateOf(LoginData.login) }
    var checked by remember { mutableStateOf(0) }
    var titles:List<Category> by remember { mutableStateOf(listOf()) }
    var addingGroup by remember { mutableStateOf(false) }
    var share by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var options by remember { mutableStateOf(false) }

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
        todos = taskRepo.getTusks(cat.id)
    }


    LaunchedEffect(category,titles,checked,LoginData.login) {
        scope.launch {
            titles = taskRepo.getCategory()
            if (s==-1&& titles.isNotEmpty()&&backCategory!=0){
                i = titles.indexOfFirst{ it.id == backCategory }
                s=0
            }
        }
        if (LoginData.login != logRe){
            i = 0
            logRe = LoginData.login
        }
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
                        share = false
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
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        Box(modifier = Modifier.padding(40.dp)) {}
                    }
                }
            }
            Column {
                AnimatedVisibility(visible = addingGroup) {
                    Column(
                        Modifier
                            .clickable { addingGroup = false }
                            .fillMaxSize(1f)
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.background.copy(
                                            alpha = 0.5F
                                        ), MaterialTheme.colorScheme.background.copy(alpha = 0.8F)
                                    )
                                )
                            )
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        GroupAdd {
                            addingGroup = false
                            titles = listOf()
                        }
                    }
                }
                AnimatedVisibility(visible = share) {
                    Column(
                        Modifier
                            .clickable { share = false }
                            .fillMaxSize(1f)
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.background.copy(
                                            alpha = 0.5F
                                        ), MaterialTheme.colorScheme.background.copy(alpha = 0.8F)
                                    )
                                )
                            )
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        ShareCategory(category.id) {
                            share = false
                        }
                    }
                }
            }
            Column {
                AnimatedVisibility(visible = (!addingGroup&&!share)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (titles.size > 1||(LoginData.token != ""&&category.shareId!=null)) {
                            IconButton(onClick = {
                                options = true
                            }) {
                                Icon(
                                    Icons.Filled.MoreVert,
                                    contentDescription = "More options vertically",
                                )
                            }
                            DropdownMenu(
                                expanded = options,
                                onDismissRequest = { options = false }
                            ) {
                                if (titles.size > 1) {
                                    DropdownMenuItem(
                                        text = {
                                            Row {
                                                Icon(
                                                    Icons.Filled.Delete,
                                                    contentDescription = "Delete Icon",
                                                    modifier = Modifier.padding(end = 10.dp)
                                                )
                                                Text("Delete")
                                            }
                                        },
                                        onClick = {
                                            scope.launch {
                                                taskRepo.delCategory(category.id)
                                                titles = taskRepo.getCategory()
                                                category = titles[0]
                                                --i
                                                options = false
                                            }
                                        }
                                    )
                                }
                                if (LoginData.token != ""&&category.shareId==null) {
                                    DropdownMenuItem(
                                        text = {
                                            Row {
                                                Icon(
                                                    Icons.Filled.Share,
                                                    contentDescription = "Share Icon",
                                                    modifier = Modifier.padding(end = 10.dp)
                                                )
                                                Text("Share")
                                            }
                                        },
                                        onClick = {
                                            share = true
                                            options = false
                                        }
                                    )
                                }
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