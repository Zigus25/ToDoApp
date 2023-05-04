package pl.mazy.todoapp.navigation

import pl.mazy.todoapp.data.model.Event
import pl.mazy.todoapp.data.model.Note


sealed interface Destinations {
    object TaskList: Destinations
    object Notes : Destinations
    object Schedule: Destinations
    object SignUp: Destinations
    object SignIn: Destinations
    class NoteDetails(val noteP: Note?) : Destinations
    class EventAdd(val event: Event?, val isTask: Boolean) : Destinations
}