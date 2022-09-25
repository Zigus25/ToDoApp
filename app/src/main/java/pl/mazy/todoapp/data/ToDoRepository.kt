package pl.mazy.todoapp.data

import pl.mazy.todoapp.Database
import pl.mazy.todoapp.logic.dataClass.Task

class ToDoRepository(
    private var database: Database
) {
    fun addCategory(taskListName: String)=
        database.todosQueries.insertCategory(taskListName)

    fun getToDos(listName:String): List<Task> =
        database.todosQueries.selectList(listName).executeAsList().map {
           Task(it.id, it.name,it.checked,it.listName)
        }

    fun addToDo(name:String,taskListName:String) =
            database.todosQueries.insertTask(name,false,taskListName)

    fun getTusk(): List<String> =
        database.todosQueries.groups().executeAsList()

    fun updateTask(newName:String, description:String,date:String,newListName:String,oldName:String,id:Long) =
        database.todosQueries.updateTask(newName,description,date,newListName,oldName,id)

    fun updateState(name: String) =
        database.todosQueries.updateState(name)

    fun deleteGroup(name:String) {
        database.todosQueries.deleteGroup(name)
        database.todosQueries.deleteFromGroup(name)
    }


    fun deleteTask(name: String) =
        database.todosQueries.deleteTask(name)
}