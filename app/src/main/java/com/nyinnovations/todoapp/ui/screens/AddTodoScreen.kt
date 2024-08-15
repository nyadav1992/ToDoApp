package com.nyinnovations.todoapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nyinnovations.todoapp.R
import com.nyinnovations.todoapp.data.TODOItem
import com.nyinnovations.todoapp.ui.viewmodel.TODOListViewModel
import kotlinx.coroutines.delay

@Composable
fun AddTodoScreen(navController: NavController, todoViewModel: TODOListViewModel) {
    var text by rememberSaveable { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var shouldLaunchEffect by remember { mutableStateOf(false) }
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = statusBarHeight) // Padding for the status bar
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text(stringResource(R.string.enter_todo_item)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            if (text.isNotBlank()) {
                if (text.equals("Error", ignoreCase = true)) {  // Use equals method with ignoreCase
                    todoViewModel.setErrorMessage("Failed to add TODO")
                    navController.popBackStack()
                } else {
                    isLoading = true
                    todoViewModel.addTODO(TODOItem(title = text))
                    shouldLaunchEffect = true
                }
            }
        }) {
            Text(stringResource(R.string.add_todo))
        }

        if (shouldLaunchEffect) {
            LaunchedEffect(Unit) {
                delay(3000)
                isLoading = false
                navController.popBackStack()
                shouldLaunchEffect = false
            }
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
        }
    }
}
