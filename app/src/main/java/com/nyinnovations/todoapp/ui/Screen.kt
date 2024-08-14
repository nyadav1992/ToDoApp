package com.nyinnovations.todoapp.ui

sealed class Screen(val route: String) {
    data object Main : Screen("main")
    data object AddTodo : Screen("add_todo")
}
