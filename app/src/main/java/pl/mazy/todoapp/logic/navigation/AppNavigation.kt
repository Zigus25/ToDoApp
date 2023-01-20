package pl.mazy.todoapp.logic.navigation

import pl.mazy.todoapp.Event

sealed interface Destinations {
    object TaskList : Destinations
    object Notes : Destinations
    object Schedule: Destinations
    class NoteDetails(val noteP: pl.mazy.todoapp.Notes?) : Destinations
    class EventAdd(val event: Event?,val isTask: Boolean) : Destinations
}