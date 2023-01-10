package pl.mazy.todoapp.logic.navigation

import pl.mazy.todoapp.Event

sealed interface Destinations {
    object TaskList : Destinations
    object Notes : Destinations
    object Schedule: Destinations
    class NoteDetails(val name:String, val des:String) : Destinations
    object CreateNote : Destinations
    class EventAdd(val event: Event?,val isTask: Boolean) : Destinations
}