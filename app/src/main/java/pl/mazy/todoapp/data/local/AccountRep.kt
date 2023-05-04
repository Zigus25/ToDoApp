package pl.mazy.todoapp.data.local

import pl.mazy.todoapp.Database

class AccountRep (
    private var database: Database
) {

    fun getActiveUser():Pair<String,String>{
        val x =database.userQueries.selectActive().executeAsOne()
        return Pair(x.token,x.login)
    }
    fun signUpUser(login:String,passwd:String,eMail:String,token: String){
        database.userQueries.insertUser(login, eMail, passwd,token)
        signInUser(login, passwd)
    }

    fun signInUser(login: String,passwd: String):Boolean{
        val sign = database.userQueries.checkUser(login, passwd).executeAsOne().toInt() == 1
        if (sign) {
            database.userQueries.toggleActive(login)
        }
        return sign
    }
    fun checkExist(eMail: String,passwd: String):Boolean{
        return database.userQueries.checkUser(eMail,passwd).executeAsOne().toInt()==0
    }

    fun signOut(login:String){
        database.userQueries.toggleActive(login)
    }
}