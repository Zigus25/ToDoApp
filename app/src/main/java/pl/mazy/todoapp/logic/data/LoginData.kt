package pl.mazy.todoapp.logic.data

object LoginData {
    var loginU :String? = null
        private set

    var userId: Long? = null
        private set

    fun logIn(login:String,id:Long){
        loginU = login
        userId = id
    }

    fun logOut(){
        loginU = null
    }
}