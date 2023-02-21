package pl.mazy.todoapp.logic.data

object LoginData {
    var loginU :String? = null
        private set

    fun logIn(login:String){
        loginU = login
    }

    fun logOut(){
        loginU = null
    }
}