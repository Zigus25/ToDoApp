package pl.mazy.todoapp.data.interfaces

import kotlinx.coroutines.flow.Flow
import pl.mazy.todoapp.data.model.Category
import pl.mazy.todoapp.data.model.Event

interface TasksInter {
    suspend fun addCategory(name:String)
    suspend fun getCategory():List<Category>
    suspend fun shareCategory(cId:Int,sId:Int)
    suspend fun delCategory(id:Int)

    suspend fun getTusks(id: Int): List<Event>

    suspend fun toggle(event: Event)
}