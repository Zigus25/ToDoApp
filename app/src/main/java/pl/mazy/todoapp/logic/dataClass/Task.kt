package pl.mazy.todoapp.logic.dataClass

data class Task (
    val ID:Int,
    val name:String,
    val checked:Boolean,
    val category:String
)