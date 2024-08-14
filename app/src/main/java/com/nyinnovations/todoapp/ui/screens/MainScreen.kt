package com.nyinnovations.todoapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nyinnovations.todoapp.ui.Screen
import com.nyinnovations.todoapp.ui.viewmodel.TODOListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, todoViewModel: TODOListViewModel) {
    val todoItems by todoViewModel.filteredTodoItems.collectAsState()
    val query by todoViewModel.searchQuery.collectAsState()

    // Get the status bar height
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    val errorMessage by todoViewModel.errorMessage.collectAsState(initial = "")

    // State to control the visibility of the AlertDialog
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }

    // Update state to show dialog when there's an error message
    if (errorMessage?.isNotEmpty() == true) {
        LaunchedEffect(errorMessage) {
            setShowDialog(true)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = statusBarHeight) // Padding for the status bar
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(8.dp)
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { todoViewModel.onSearchQueryChanged(it) },
                    label = { Text("Search TODOs", color = Color.White.copy(alpha = 0.5f)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedTextColor = Color.White,
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White.copy(alpha = 0.5f),
                        focusedBorderColor = Color.White, // Transparent background
                        unfocusedBorderColor = Color.Gray, // Transparent background
                    ),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = Color.White
                        )
                    }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Screen.AddTodo.route) }) {
                Icon(Icons.Filled.Add, contentDescription = "Add TODO")
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                if (todoItems.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            "Press the + button to add a TODO item",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(todoItems) { todoItem ->
                            Text(
                                text = todoItem.title,
                                modifier = Modifier
                                    .padding(vertical = 4.dp)
                                    .background(
                                        MaterialTheme.colorScheme.surfaceDim,
                                        RoundedCornerShape(2.dp)
                                    )
                                    .border(
                                        0.2.dp,
                                        MaterialTheme.colorScheme.outline,
                                        RoundedCornerShape(2.dp)
                                    )
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    )

    // Observe error event
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { setShowDialog(false) },
            title = { Text(text = "Error") },
            text = { errorMessage?.let { Text(text = it) } },
            confirmButton = {
                Button(
                    onClick = {
                        setShowDialog(false)
                        todoViewModel.clearErrorMessage() // Clear the error message after showing the dialog
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

}
