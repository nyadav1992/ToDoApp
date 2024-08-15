package com.nyinnovations.todoapp.data.repo

import com.nyinnovations.todoapp.data.TODODao
import com.nyinnovations.todoapp.data.TODOItem
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(JUnit4::class)
class TodoRepositoryTest {

    private lateinit var repository: TODORepository
    private lateinit var todoDao: TODODao

    @Before
    fun setup() {
        todoDao = mock(TODODao::class.java)
        repository = TODORepository(todoDao)
    }

    @Test
    fun getTodos_returnsFlowOfTodoItems() = runTest {
        val todoList = listOf(TODOItem(1, "Test 1"), TODOItem(2, "Test 2"))
        whenever(todoDao.getAllTODOs()).thenReturn(flowOf(todoList))

        val result = repository.getTODOs().first()

        assertEquals(todoList, result)
    }

    @Test
    fun addTodoItem_insertsItemIntoDatabase() = runTest {
        val todoItem = TODOItem(1, "Test 1")

        repository.addTODO(todoItem)

        verify(todoDao).insertTODO(todoItem)
    }

}
