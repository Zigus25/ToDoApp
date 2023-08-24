package pl.mazy.todoapp.data.remote.repos

import pl.mazy.todoapp.data.LoginData
import pl.mazy.todoapp.data.interfaces.TasksInter
import pl.mazy.todoapp.data.model.Category
import pl.mazy.todoapp.data.model.Event
import pl.mazy.todoapp.data.remote.TDAService
import java.lang.Exception

class TasksRepo(private val api: TDAService):TasksInter {
    override suspend fun addCategory(name: String) {
        api.postCategory(LoginData.token,name)
    }

    override suspend fun getCategory(): List<Category> {
        return try {
            api.getCategories(LoginData.token)
        }catch (e:Exception){
            listOf()
        }
    }

    override suspend fun shareCategory(cId: Int, sId: Int) {
        api.shareCategory(LoginData.token,cId,sId)
    }

    override suspend fun delCategory(id: Int) {
        api.deleteCategory(LoginData.token,id)
    }

    override suspend fun getTusks(id: Int): List<Event> {
        return api.getTasksByCat(LoginData.token,id)
    }

    override suspend fun unmarkAll(ev: Event) {
        api.unmarkAll(LoginData.token,ev)
    }

    override suspend fun toggle(event: Event) {
        api.toggleTask(LoginData.token,event)
    }
}