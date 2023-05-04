package pl.mazy.todoapp.data

object LoginData {
    var login :String? = null
        private set

    var token: String = ""
        private set

    fun logIn(l:String,t:String){
        login = l
        token = t
    }

    fun logOut(){
        login = null
        token = ""
    }
}