package pl.mazy.todoapp.data

object LoginData {
    var login :String? = null
        private set

    var token: String = ""
        private set
    var sid: Int? = null
        private set

    fun logIn(l:String?,t:String,s:Int?){
        login = l
        token = t
        sid = s
    }

    fun logOut(){
        login = null
        token = ""
        sid = null
    }
}