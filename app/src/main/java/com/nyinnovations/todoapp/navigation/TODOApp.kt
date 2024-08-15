package com.nyinnovations.todoapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nyinnovations.todoapp.ui.screens.AddTodoScreen
import com.nyinnovations.todoapp.ui.screens.MainScreen
import com.nyinnovations.todoapp.ui.viewmodel.TODOListViewModel

@Composable
fun TODOApp(todoViewModel: TODOListViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Main.route) {
        composable(Screen.Main.route) {
            MainScreen(navController, todoViewModel)
        }
        composable(Screen.AddTodo.route) {
            AddTodoScreen(navController, todoViewModel)
        }
    }
}