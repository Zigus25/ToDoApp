package pl.mazy.todoapp.logic.data

data class Event(
    val id: Long,
    val Name: String,
    val Description: String,
    val Category: String,
    val TimeStart: String?,
    val TimeEnd: String?,
    val DateStart: String?,
    val DateEnd: String?,
    val Type: Boolean,
    val Checked: Boolean,
    val Color: String,
    val MainTaskID: Long?,
    val SubList:List<Event>
)
