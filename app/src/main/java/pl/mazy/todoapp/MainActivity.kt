package pl.mazy.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.kodein.di.compose.withDI
import pl.mazy.todoapp.logic.navigation.Destinations
import pl.mazy.todoapp.logic.navigation.NavController
import pl.mazy.todoapp.ui.components.note.NoteAdding
import pl.mazy.todoapp.ui.components.task.TaskEdit
import pl.mazy.todoapp.ui.theme.ToDoAPpTheme
import pl.mazy.todoapp.ui.views.NoteList
import pl.mazy.todoapp.ui.views.Schedule
import pl.mazy.todoapp.ui.views.TaskList

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            withDI((application as ToDoApplication).di) {
                ToDoAPpTheme {
                    val controller: NavController<Destinations> by remember {
                        mutableStateOf(NavController(Destinations.TaskList))
                    }
                    BackHandler(!controller.isLast()) {
                        controller.pop()
                    }
                    Column {
                        Box(modifier = Modifier.weight(1f)) {
                            when (val x = controller.currentBackStackEntry.value) {
                                is Destinations.TaskList -> TaskList(controller)
                                is Destinations.NoteDetails -> NoteAdding(controller,x.name,x.des)
                                is Destinations.TaskDetails -> TaskEdit(controller,x.task)
                                is Destinations.Notes -> NoteList(controller)
                                is Destinations.Schedule -> Schedule(controller)
                                is Destinations.CreateNote -> NoteAdding(controller,"","")
                            }
                        }
                    }
                }
            }
        }
    }
}