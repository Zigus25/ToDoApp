package pl.mazy.todoapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val description: String?,
    val id: Int?,
    val name: String,
    val owner_id: Int?
)