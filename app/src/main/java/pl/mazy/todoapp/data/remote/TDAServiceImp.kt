package pl.mazy.todoapp.data.remote


import io.ktor.client.*
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
        client.get<HttpResponse>("https://mazy.dev/auth/logout") {
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
        client.post<HttpResponse>("https://mazy.dev/category/$name"){
            headers{
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
    }

    override suspend fun deleteCategory(token: String, id: Int) {
        client.delete<HttpResponse>("https://mazy.dev/category/$id"){
            headers{
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
    }

    override suspend fun shareCategory(token: String, cId: Int, sId: Int) {
        client.post<HttpResponse>("https://mazy.dev/category/cID=$cId/sID=$sId"){
            headers{
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
    }

    //Calendar
    override suspend fun deleteEvent(token: String, ev: Event) {
        client.delete<HttpResponse>("https://mazy.dev/events"){
            contentType(ContentType.Application.Json)
            body = ev
            headers{
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
    }

    override suspend fun newEvent(token: String, ev: Event) {
        client.post<AuthResponse> {
            url("https://mazy.dev/events")
            contentType(ContentType.Application.Json)
            body = ev
            headers{
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
    }

    override suspend fun getByDate(token: String, date: String): List<Event> {
        return client.get("https://mazy.dev/events/d=$date"){
            headers{
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
    }

    override suspend fun getTasksByCat(token: String, cId: Int): List<Event> {
        return client.get("https://mazy.dev/events/$cId"){
            headers{
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
    }

    override suspend fun toggleTask(token: String, ev: Event) {
        client.post<AuthResponse> {
            url("https://mazy.dev/events/t")
            contentType(ContentType.Application.Json)
            body = ev
            headers{
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
    }

    //Notes
    override suspend fun newNote(token: String, note: Note) {
        client.post<AuthResponse> {
            url("https://mazy.dev/notes")
            contentType(ContentType.Application.Json)
            body = note
            headers{
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
    }

    override suspend fun getNotes(token: String): List<Note> {
        return client.get("https://mazy.dev/notes"){
            headers{
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
    }

    override suspend fun delNote(token: String, nId: Int) {
        client.delete<HttpResponse>("https://mazy.dev/notes/$nId"){
            headers{
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
    }

}