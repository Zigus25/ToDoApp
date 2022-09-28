package pl.mazy.todoapp.logic.dataClass

data class Task(
    val ID: Long,
    val name:String,
    val checked:Boolean,
    val subList: String?,
    val category:String
)