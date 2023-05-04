package pl.mazy.todoapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val category_id: Int,
    val checked: Boolean,
    val color: String,
    val dateEnd: String?,
    val dateStart: String?,
    val description: String?,
    val id: Int?,
    val mainTask_id: Int?,
    val name: String,
    val owner_id: Int?,
    val timeEnd: String?,
    val timeStart: String?,
    val type: Boolean,
    val subList:List<Event>
)