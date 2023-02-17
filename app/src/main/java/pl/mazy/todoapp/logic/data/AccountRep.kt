package pl.mazy.todoapp.logic.data

import pl.mazy.todoapp.Database

var uLogin = ""
class AccountRep (
    private var database: Database
) {
    fun signUpUser(login:String,passwd:String,eMail:String){
        database.userQueries.insertUser(login, eMail, passwd)
    }

    fun signInUser(login: String,passwd: String):Boolean{
        val tf =  database.userQueries.checkUser(login, passwd).equals(1)
        if (tf){
            uLogin = login
        }
        return tf
    }

    fun signOut(){
        uLogin = ""
    }
}