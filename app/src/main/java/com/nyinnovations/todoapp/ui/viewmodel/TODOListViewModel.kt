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

    private val _filteredTodoItems = MutableStateFlow<List<TODOItem>>(emptyList())
    val filteredTodoItems: StateFlow<List<TODOItem>> = _filteredTodoItems

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        viewModelScope.launch {
            repository.getTODOs().collect { todos ->
                _todoItems.value = todos
                filterTodos()
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
            repository.addTODO(todoItem)
            repository.getTODOs().collect { todos ->
                _todoItems.value = todos
                filterTodos()
            }
        }
    }

    fun setErrorMessage(message: String) {
        _errorMessage.value = message
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}
