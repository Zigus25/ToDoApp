package pl.mazy.todoapp.data.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val login: String,
    val sid: Int,
    val access_token: String
)