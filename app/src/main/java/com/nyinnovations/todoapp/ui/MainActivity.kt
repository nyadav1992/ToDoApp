package com.nyinnovations.todoapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nyinnovations.todoapp.ui.screens.AddTodoScreen
import com.nyinnovations.todoapp.ui.screens.MainScreen
import com.nyinnovations.todoapp.ui.viewmodel.TODOListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val todoViewModel: TODOListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TODOApp(todoViewModel)
        }
    }
}

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