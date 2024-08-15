package com.nyinnovations.todoapp.navigation

sealed class Screen(val route: String) {
    data object Main : Screen("main")
    data object AddTodo : Screen("add_todo")
}
