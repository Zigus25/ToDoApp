package pl.mazy.todoapp.logic.data.repos

import pl.mazy.todoapp.Database

class AccountRep (
    private var database: Database
) {

    fun getActiveUser():Pair<Long,String>{
        val x =database.userQueries.selectActive().executeAsOne()
        return Pair(x.id,x.login)
    }
    fun signUpUser(login:String,passwd:String,eMail:String){
        database.userQueries.insertUser(login, eMail, passwd)
        signInUser(login, passwd)
    }

    fun signInUser(login: String,passwd: String):Boolean{
        val sign = database.userQueries.checkUser(login, passwd).executeAsOne().toInt() == 1
        if (sign) {
            database.userQueries.toggleActive(login)
        }
        return sign
    }

    fun signOut(login:String){
        database.userQueries.toggleActive(login)
    }
}