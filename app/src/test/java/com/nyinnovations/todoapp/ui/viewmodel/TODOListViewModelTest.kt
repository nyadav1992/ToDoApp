package com.nyinnovations.todoapp.ui.viewmodel


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nyinnovations.todo_datasource.data.TODOItem
import com.nyinnovations.todo_datasource.data.repo.TODORepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.verify

@RunWith(JUnit4::class)
class TODOListViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: TODORepository

    private lateinit var viewModel: TODOListViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock(TODORepository::class.java)
        `when`(repository.getTODOs()).thenReturn(flowOf(emptyList()))
        viewModel = TODOListViewModel(repository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset the main dispatcher to the original Main dispatcher
    }

    @Test
    fun `init should load TODO items`() = runTest {
        val todos = listOf(TODOItem(1, "Task 1"), TODOItem(2, "Task 2"))
        `when`(repository.getTODOs()).thenReturn(flow { emit(todos) })

        viewModel = TODOListViewModel(repository)  // Re-initialize ViewModel after setting up mock

        assertEquals(todos, viewModel.todoItems.value)
        assertEquals(todos, viewModel.filteredTodoItems.value)
    }

    @Test
    fun `onSearchQueryChanged should filter TODO items`() = runTest {
        val todos = listOf(TODOItem(1, "Buy milk"), TODOItem(2, "Walk the dog"))
        `when`(repository.getTODOs()).thenReturn(flow { emit(todos) })

        viewModel = TODOListViewModel(repository)
        viewModel.onSearchQueryChanged("Buy")

        assertEquals(listOf(todos[0]), viewModel.filteredTodoItems.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `addTODO should add item and refresh list`() = runTest {
        // Prepare initial TODO items
        val initialTodos = listOf(TODOItem(1, "Task 1"), TODOItem(2, "Task 2"))
        val newTodoItem = TODOItem(3, "New Task")
        val updatedTodos = initialTodos + newTodoItem

        // Mock repository behavior
        `when`(repository.getTODOs()).thenReturn(flow {
            emit(updatedTodos)
        })

        viewModel = TODOListViewModel(repository)

        // Act: Add the new TODO item
        viewModel.addTODO(newTodoItem)

        // Wait until all coroutines have completed
        advanceUntilIdle()

        // Verify repository calls and check final state
        verify(repository).addTODO(newTodoItem)
        assertEquals(updatedTodos, viewModel.todoItems.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `deleteTodoItem should delete item and refresh list`() = runTest {
        val initialTodos = listOf(TODOItem(1, "Task 1"), TODOItem(2, "Task 2"))
        val itemToDelete = TODOItem(1, "Task 1")
        val updatedTodos = listOf(TODOItem(2, "Task 2"))

        `when`(repository.getTODOs()).thenReturn(flowOf(initialTodos), flowOf(updatedTodos))

        viewModel = TODOListViewModel(repository)

        viewModel.deleteTodoItem(itemToDelete)
        advanceUntilIdle()

        verify(repository).deleteTODO(itemToDelete)
        assertEquals(updatedTodos, viewModel.todoItems.value)
    }

    @Test
    fun `setErrorMessage should update errorMessage`() {
        val errorMessage = "An error occurred"

        viewModel.setErrorMessage(errorMessage)

        assertEquals(errorMessage, viewModel.errorMessage.value)
    }

    @Test
    fun `clearErrorMessage should clear errorMessage`() {
        viewModel.setErrorMessage("An error occurred")
        viewModel.clearErrorMessage()

        assertNull(viewModel.errorMessage.value)
    }
}