package pl.mazy.todoapp.logic.data

import pl.mazy.todoapp.Database

class AccountRep (
    private var database: Database
) {
    fun signUpUser(login:String,passwd:String,eMail:String){
        database.userQueries.insertUser(login, eMail, passwd)
    }

    fun signInUser(login: String,passwd: String):Boolean{
        return database.userQueries.checkUser(login, passwd).executeAsOne().toInt() == 1
    }

    fun signOut(){
    }
}