package pl.mazy.todoapp.data.remote


import io.ktor.client.*
import io.ktor.client.features.get
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import pl.mazy.todoapp.data.model.Category
import pl.mazy.todoapp.data.model.Event
import pl.mazy.todoapp.data.model.Note
import pl.mazy.todoapp.data.remote.model.request.AuthReq
import pl.mazy.todoapp.data.remote.model.request.SingUpReq
import pl.mazy.todoapp.data.remote.model.response.AuthResponse

class TDAServiceImp(
    private val client: HttpClient
) :TDAService {

    //auth
    override suspend fun auth(authReq: AuthReq): AuthResponse {
        return client.post<AuthResponse> {
            url("https://mazy.dev/auth/authenticate")
            contentType(ContentType.Application.Json)
            body = authReq
        }
    }

    override suspend fun signup(singUpReq: SingUpReq): AuthResponse {
        return client.post<AuthResponse> {
            url("https://mazy.dev/auth/register")
            contentType(ContentType.Application.Json)
            body = singUpReq
        }
    }

    override suspend fun logout(token: String) {
        val r:HttpResponse = client.get("https://mazy.dev/auth/logout") {
            headers{
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
    }

    //categories
    override suspend fun getCategories(token: String): List<Category> {
        return client.get("https://mazy.dev/category"){
            headers{
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
    }

    override suspend fun postCategory(token: String, name: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCategory(token: String, id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun shareCategory(token: String, cId: Int, sId: Int) {
        TODO("Not yet implemented")
    }

    //Calendar
    override suspend fun deleteEvent(token: String, eId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun newEvent(token: String, ev: Event) {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskByID(token: String, eId: Int): Event {
        TODO("Not yet implemented")
    }

    override suspend fun getByDate(token: String, date: String): List<Event> {
        TODO("Not yet implemented")
    }

    override suspend fun getTasksByCat(token: String, cId: Int): List<Event> {
        return client.get("https://mazy.dev/events/$cId"){
            headers{
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
    }

    override suspend fun toggleTask(token: String, ev: Event) {
        TODO("Not yet implemented")
    }

    override suspend fun changeFalse(token: String, eId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun changeTrue(token: String, eId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getInMainTask(token: String, mId: Int): List<Event> {
        TODO("Not yet implemented")
    }

    override suspend fun getNamesInMainTask(token: String, mId: Int): List<Event> {
        TODO("Not yet implemented")
    }

    //Notes
    override suspend fun newNote(token: String, note: Note) {
        TODO("Not yet implemented")
    }

    override suspend fun getNotes(token: String) {
        TODO("Not yet implemented")
    }

    override suspend fun delNote(token: String, nId: Int) {
        TODO("Not yet implemented")
    }

}