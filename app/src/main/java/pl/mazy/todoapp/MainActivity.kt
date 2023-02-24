package pl.mazy.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.kodein.di.compose.localDI
import org.kodein.di.compose.withDI
import org.kodein.di.instance
import pl.mazy.todoapp.logic.data.LoginData
import pl.mazy.todoapp.logic.data.repos.AccountRep
import pl.mazy.todoapp.logic.navigation.Destinations
import pl.mazy.todoapp.logic.navigation.NavController
import pl.mazy.todoapp.ui.components.calendar.EventAddEdit
import pl.mazy.todoapp.ui.components.note.NoteAdding
import pl.mazy.todoapp.ui.theme.ToDoAPpTheme
import pl.mazy.todoapp.ui.views.NoteList
import pl.mazy.todoapp.ui.views.Schedule
import pl.mazy.todoapp.ui.views.SignIn
import pl.mazy.todoapp.ui.views.SignUp
import pl.mazy.todoapp.ui.views.TaskList
import java.lang.NullPointerException

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            withDI((application as ToDoApplication).di) {
                ToDoAPpTheme {
                    val userRepository: AccountRep by localDI().instance()
                    try {
                        val pair = userRepository.getActiveUser()
                        LoginData.logIn(pair.second,pair.first)
                    }catch (_:NullPointerException){ }
                    var program by remember {
                        mutableStateOf("Task List")
                    }

                    val controller: NavController<Destinations> by remember {
                        mutableStateOf(NavController(Destinations.TaskList))
                    }
                    BackHandler(!controller.isLast()) {
                        controller.pop()
                    }
                    val scope = rememberCoroutineScope()
                    val drawerState = rememberDrawerState(DrawerValue.Closed)
                    BackHandler( enabled = drawerState.isOpen) {
                        scope.launch {
                            drawerState.close()
                        }
                    }
                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = {
                            Spacer(Modifier.padding(25.dp))
                            NavigationDrawerItem(
                                icon = {Icon(
                                    Icons.Filled.Checklist,
                                    contentDescription = "Calendar Icon",
                                )},
                                label = { Text("Tasks") },
                                selected = program == "Tasks",
                                onClick = {
                                    program = "Tasks"
                                    scope.launch { drawerState.close() }
                                    controller.navigate(Destinations.TaskList)
                                },
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                            )
                            NavigationDrawerItem(
                                icon = {Icon(
                                    Icons.Filled.Edit,
                                    contentDescription = "Calendar Icon",
                                )},
                                label = { Text("Notes") },
                                selected = program == "Notes",
                                onClick = {
                                    program = "Notes"
                                    scope.launch { drawerState.close() }
                                    controller.navigate(Destinations.Notes)
                                },
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                            )
                            NavigationDrawerItem(
                                icon = {Icon(
                                    Icons.Filled.CalendarMonth,
                                    contentDescription = "Calendar Icon",
                                )},
                                label = { Text("Calendar") },
                                selected = program == "Calendar",
                                onClick = {
                                    program = "Calendar"
                                    scope.launch { drawerState.close() }
                                    controller.navigate(Destinations.Schedule)
                                },
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 30.dp,bottom = 20.dp)
                            ) {
                                if (LoginData.loginU == null){
                                    Text(text = "Sign In", modifier = Modifier.clickable {
                                        scope.launch { drawerState.close() }
                                        controller.navigate(Destinations.SignIn) },
                                        fontSize = 30.sp)
                                }else{
                                    Text(text = LoginData.loginU!!, modifier = Modifier.clickable {
                                        scope.launch { drawerState.close() }
                                        userRepository.signOut(LoginData.loginU!!)
                                        LoginData.logOut()
                                   },
                                        fontSize = 30.sp)
                                }
                            }
                        },
                        content = {
                            Column {
                                Row(modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.background), verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = {
                                        scope.launch { drawerState.open() }
                                    }) {
                                        Icon(Icons.Filled.Menu, contentDescription = "Menu Icon", tint = MaterialTheme.colorScheme.onBackground)
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(text = program, fontSize = 24.sp, color = MaterialTheme.colorScheme.onBackground)
                                    Spacer(modifier = Modifier.weight(1f))
                                    IconButton(onClick = {
                                            if (LoginData.loginU == null){
                                                controller.navigate(Destinations.SignIn)
                                            }else{
                                                userRepository.signOut(LoginData.loginU!!)
                                                LoginData.logOut()
                                        }}) {
                                        Icon(
                                            Icons.Filled.SupervisedUserCircle,
                                            contentDescription = "Profile icon",
                                            tint = MaterialTheme.colorScheme.onBackground
                                        )
                                    }
                                }
                                Box(modifier = Modifier.weight(1f)) {
                                    when (val x = controller.currentBackStackEntry.value) {
                                        is Destinations.TaskList ->{
                                            TaskList(controller)
                                            program = "Tasks"
                                        }
                                        is Destinations.NoteDetails -> NoteAdding(controller,x.noteP)
                                        is Destinations.Notes ->{
                                            NoteList(controller)
                                            program = "Notes"
                                        }
                                        is Destinations.Schedule -> {
                                            Schedule(controller)
                                            program = "Calendar"
                                        }
                                        is Destinations.EventAdd -> EventAddEdit(controller,x.event,x.isTask)
                                        is Destinations.SignIn -> {
                                            SignIn(controller)
                                            program = ""
                                        }
                                        is Destinations.SignUp -> {
                                            SignUp(controller)
                                            program = ""
                                        }
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}