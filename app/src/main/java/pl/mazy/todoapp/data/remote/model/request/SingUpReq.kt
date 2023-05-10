package pl.mazy.todoapp.data.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class SingUpReq(
    val email: String,
    val name: String,
    val passwd: String
)