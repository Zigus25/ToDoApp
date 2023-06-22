package pl.mazy.todoapp.data.local

import pl.mazy.todoapp.Database
import pl.mazy.todoapp.User
import pl.mazy.todoapp.data.remote.model.response.AuthResponse

class AccountRep (
    private var database: Database
) {

    fun getActiveUser():User?{
        return database.userQueries.selectActive().executeAsOneOrNull()
    }

    fun getUsers():List<User>{
        return database.userQueries.getUsers().executeAsList()
    }

    fun signUpUser(login:String,passwd:String,eMail:String,sid:Int){
        database.userQueries.insertUser(login, eMail, passwd,sid.toLong())
        signInUser(sid)
    }

    fun signInUser(sid: Int){
        database.userQueries.toggleActive(sid.toLong())
    }
    fun checkExist(eMail: String,passwd: String):Boolean{
        return database.userQueries.checkUser(eMail,passwd).executeAsOne().toInt()==0
    }

    fun signOut(sid:Int){
        database.userQueries.toggleFalse(sid.toLong())
    }
}