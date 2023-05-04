package pl.mazy.todoapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: Int,
    val name: String,
    val ownerId: Int,
    val shareId: Int?
)