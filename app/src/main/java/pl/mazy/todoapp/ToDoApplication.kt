package pl.mazy.todoapp

import android.app.Application
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.androidXModule
import pl.mazy.todoapp.di.mainModule

class ToDoApplication: Application(), DIAware {
    override val di by DI.lazy {
        import(androidXModule(this@ToDoApplication))
        import(mainModule)
    }
}
