package pl.mazy.todoapp.logic

import com.squareup.sqldelight.android.AndroidSqliteDriver
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import pl.mazy.todoapp.Database
import pl.mazy.todoapp.logic.data.repos.AccountRep
import pl.mazy.todoapp.logic.data.repos.NotesRepository
import pl.mazy.todoapp.logic.data.repos.CalendarRepository
import pl.mazy.todoapp.logic.data.repos.ToDoRepository


val mainModule = DI.Module("main") {
    bindSingleton {
        Database(
            AndroidSqliteDriver(Database.Schema, instance(), "base.db")
        )
    }
    bindSingleton { ToDoRepository(instance()) }
    bindSingleton { NotesRepository(instance()) }
    bindSingleton { CalendarRepository(instance()) }
    bindSingleton { AccountRep(instance()) }
}
