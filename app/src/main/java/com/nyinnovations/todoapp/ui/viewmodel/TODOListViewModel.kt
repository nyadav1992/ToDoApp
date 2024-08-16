package com.nyinnovations.todoapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyinnovations.todoapp.data.TODOItem
import com.nyinnovations.todoapp.data.repo.TODORepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TODOListViewModel @Inject constructor(
    private val repository: TODORepository
) : ViewModel() {

    private val _todoItems = MutableStateFlow<List<TODOItem>>(emptyList())
    val todoItems: StateFlow<List<TODOItem>> = _todoItems

    private val _filteredTodoItems = MutableStateFlow<List<TODOItem>>(emptyList())
    val filteredTodoItems: StateFlow<List<TODOItem>> = _filteredTodoItems

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        viewModelScope.launch {
            try {
                getAllToDos()
            } catch (e: Exception) {
                setErrorMessage("Failed to load TODO items: ${e.message}")
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        filterTodos()
    }

    private fun filterTodos() {
        val query = _searchQuery.value.lowercase()
        _filteredTodoItems.value = if (query.isEmpty()) {
            _todoItems.value
        } else {
            _todoItems.value.filter { it.title.lowercase().contains(query) }
        }
    }

    fun addTODO(todoItem: TODOItem) {
        viewModelScope.launch {
            try {
                repository.addTODO(todoItem)
                getAllToDos()
            } catch (e: Exception) {
                setErrorMessage("Failed to add TODO item: ${e.message}")
            }
        }
    }

    private suspend fun getAllToDos() {
        try {
            repository.getTODOs().collect { todos ->
                _todoItems.value = todos
                filterTodos()
            }
        } catch (e: Exception) {
            setErrorMessage("Failed to fetch TODO items: ${e.message}")
        }
    }

    fun setErrorMessage(message: String) {
        _errorMessage.value = message
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}