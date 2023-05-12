package pl.mazy.todoapp.di

import com.squareup.sqldelight.android.AndroidSqliteDriver
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import pl.mazy.todoapp.Database
import pl.mazy.todoapp.data.local.CalendarRepoLocal
import pl.mazy.todoapp.data.local.NotesRepoLocal
import pl.mazy.todoapp.data.local.TasksRepoLocal
import pl.mazy.todoapp.data.local.AccountRep
import pl.mazy.todoapp.data.remote.TDAService
import pl.mazy.todoapp.data.remote.repos.CalendarRepo
import pl.mazy.todoapp.data.remote.repos.NotesRepo
import pl.mazy.todoapp.data.remote.repos.TasksRepo


val mainModule = DI.Module("main") {
    bindSingleton {
        Database(
            AndroidSqliteDriver(Database.Schema, instance(), "base.db")
        )
    }
    bindSingleton { TasksRepoLocal(instance()) }
    bindSingleton { NotesRepoLocal(instance()) }
    bindSingleton { CalendarRepoLocal(instance()) }
    bindSingleton { AccountRep(instance()) }
    bindSingleton { TDAService.create() }
    bindSingleton { TasksRepo(instance()) }
    bindSingleton { NotesRepo(instance()) }
    bindSingleton { CalendarRepo(instance()) }
}
