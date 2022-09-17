package pl.mazy.todoapp.data

import pl.mazy.todoapp.Database
import pl.mazy.todoapp.Tasks

class ToDoRepository(
    private var database: Database
) {
    fun getToDos(listName:String): List<Tasks> =
        database.todosQueries.selectList(listName).executeAsList().map {
           it
        }

    fun addToDo(name:String,taskListName:String) =
            database.todosQueries.insert(name,false,taskListName)

    fun getTusk(): List<String> =
        database.todosQueries.listOfTask().executeAsList()

    fun updateState(name: String) =
        database.todosQueries.updateState(name)

    fun deleteTask(name: String) =
        database.todosQueries.deleteTask(name)
}