package pl.mazy.todoapp.data.remote.repos

import kotlinx.coroutines.flow.Flow
import pl.mazy.todoapp.data.LoginData
import pl.mazy.todoapp.data.interfaces.TasksInter
import pl.mazy.todoapp.data.model.Category
import pl.mazy.todoapp.data.model.Event
import pl.mazy.todoapp.data.remote.TDAService

class TasksRepo(private val api: TDAService):TasksInter {
    private val token = LoginData.token
    override suspend fun addCategory(name: String) {
        api.postCategory(token,name)
    }

    override suspend fun getCategory(): List<Category> {
        return api.getCategories(token)
    }

    override suspend fun delCategory(id: Int) {
        api.deleteCategory(token,id)
    }

    override suspend fun getTusks(id: Int): List<Event> {
        return api.getTasksByCat(token,id)
    }

    override suspend fun toggle(event: Event) {
        api.toggleTask(token,event)
    }
}