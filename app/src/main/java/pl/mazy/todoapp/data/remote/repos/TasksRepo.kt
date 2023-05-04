package pl.mazy.todoapp.data.remote.repos

import kotlinx.coroutines.flow.Flow
import pl.mazy.todoapp.data.LoginData
import pl.mazy.todoapp.data.interfaces.TasksInter
import pl.mazy.todoapp.data.model.Category
import pl.mazy.todoapp.data.model.Event
import pl.mazy.todoapp.data.remote.TDAService

class TasksRepo(val api: TDAService):TasksInter {
    val loginData = LoginData
    override suspend fun addCategory(name: String) {
        api.postCategory(loginData.token,name)
    }

    override suspend fun getCategory(): List<Category> {
        TODO("Not yet implemented")
    }

    override suspend fun delCategory(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getTusks(id: Int): List<Event> {
        TODO("Not yet implemented")
    }

    override suspend fun toggle(id: Int) {
        TODO("Not yet implemented")
    }
}