package pl.mazy.todoapp.data.remote.model.request

data class SingUpReq(
    val email: String,
    val name: String,
    val passwd: String
)