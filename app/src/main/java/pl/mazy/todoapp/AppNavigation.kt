package pl.mazy.todoapp

sealed interface Destinations {
    object TaskList : Destinations
    object Notes : Destinations
    class Details(val name:String,val des:String) : Destinations
    object CreateNote : Destinations
}