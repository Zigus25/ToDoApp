package pl.mazy.todoapp.data.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class AuthReq(
    val email: String,
    val passwd: String
)