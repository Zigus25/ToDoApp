package pl.mazy.todoapp.logic.navigation

import pl.mazy.todoapp.logic.data.Event


sealed interface Destinations {
    object TaskList: Destinations
    object Notes : Destinations
    object Schedule: Destinations
    object SignUp:Destinations
    object SignIn: Destinations
    class NoteDetails(val noteP: pl.mazy.todoapp.Notes?) : Destinations
    class EventAdd(val event: Event?, val isTask: Boolean) : Destinations
}