package pl.mazy.todoapp.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import pl.mazy.todoapp.data.model.Category
import pl.mazy.todoapp.data.model.Event
import pl.mazy.todoapp.data.model.Note
import pl.mazy.todoapp.data.remote.model.request.AuthReq
import pl.mazy.todoapp.data.remote.model.request.SingUpReq
import pl.mazy.todoapp.data.remote.model.response.AuthResponse

interface TDAService {
    //auth
    suspend fun auth(authReq: AuthReq):AuthResponse?
    suspend fun signup(singUpReq: SingUpReq):AuthResponse?
    suspend fun logout(token:String)

    //categories
    suspend fun getCategories(token:String):List<Category>
    suspend fun postCategory(token: String, name:String)
    suspend fun deleteCategory(token: String,id:Int)
    suspend fun shareCategory(token:String,cId:Int,sId:Int)

    //calendar
    suspend fun deleteEvent(token: String,eId:Int)
    suspend fun newEvent(token: String,ev:Event)
    suspend fun getTaskByID(token: String,eId: Int):Event

        //Events
        suspend fun getByDate(token: String,date: String):List<Event>

        //Tasks
        suspend fun getTasksByCat(token: String,cId: Int):List<Event>
        suspend fun toggleTask(token: String,ev: Event)
        suspend fun changeFalse(token: String,eId: Int)
        suspend fun changeTrue(token: String,eId: Int)
        suspend fun getInMainTask(token: String,mId: Int):List<Event>
        suspend fun getNamesInMainTask(token: String,mId: Int):List<Event>

    //Notes
    suspend fun newNote(token: String,note: Note)
    suspend fun getNotes(token: String)
    suspend fun delNote(token: String,nId:Int)
    companion object{
        fun create():TDAService{
            return TDAServiceImp(
                client = HttpClient(Android){
                    install(JsonFeature){
                        serializer = KotlinxSerializer()
                    }
                }
            )
        }
    }
}