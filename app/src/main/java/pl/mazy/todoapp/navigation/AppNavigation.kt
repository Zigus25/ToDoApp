package pl.mazy.todoapp.navigation

import pl.mazy.todoapp.User
import pl.mazy.todoapp.data.model.Event
import pl.mazy.todoapp.data.model.Note


sealed interface Destinations {
    class TaskList(val bacCat:Int,val hiddenD:List<Int>): Destinations
    object Notes : Destinations
    object Schedule: Destinations
    object SignUp: Destinations
    class SignIn(val user: User?): Destinations
    class NoteDetails(val noteP: Note?) : Destinations
    class EventAdd(val event: Event?, val isTask: Boolean, val cId:Int?, val hid:List<Int>) : Destinations
}