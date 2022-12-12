package pl.mazy.todoapp.logic.navigation

import androidx.compose.runtime.mutableStateOf

class NavController<DestinationBase>(beginning: DestinationBase) {
    private val backStack = mutableListOf(beginning)
    fun pop() {
        if (backStack.size > 1) backStack.removeLast()
        currentBackStackEntry.value = backStack.last()
    }

    var currentBackStackEntry = mutableStateOf(beginning)


    fun navigate(destination: DestinationBase) {
        backStack.add(destination)
        currentBackStackEntry.value = destination
    }

    fun absoluteNavigate(destination: DestinationBase) {
        backStack.clear()
        navigate(destination)
    }
    fun isLast() = backStack.size == 1
}