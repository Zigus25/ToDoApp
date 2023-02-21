package pl.mazy.todoapp.logic.data

import pl.mazy.todoapp.Database

class AccountRep (
    private var database: Database
) {
    var loginU:String = ""

    fun getActiveUser(){
        loginU = database.userQueries.selectActive().executeAsOne()
    }
    fun signUpUser(login:String,passwd:String,eMail:String){
        database.userQueries.insertUser(login, eMail, passwd)
    }

    fun signInUser(login: String,passwd: String):Boolean{
        val sign = database.userQueries.checkUser(login, passwd).executeAsOne().toInt() == 1
        if (sign) {
            database.userQueries.toggleActive(login)
            loginU = login
        }
        return sign
    }

    fun signOut(){
        database.userQueries.toggleActive(loginU)
        loginU = ""
    }
}