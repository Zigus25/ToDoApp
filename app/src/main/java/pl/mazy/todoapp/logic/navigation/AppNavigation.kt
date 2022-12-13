package pl.mazy.todoapp.logic.navigation

import pl.mazy.todoapp.logic.dataClass.Task

sealed interface Destinations {
    object TaskList : Destinations
    object Notes : Destinations
    object Schedule: Destinations
    class NoteDetails(val name:String, val des:String) : Destinations
    class TaskDetails(val task: Task): Destinations
    object CreateNote : Destinations
}