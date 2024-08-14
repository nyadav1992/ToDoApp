package com.nyinnovations.todoapp.data.repo

import com.nyinnovations.todoapp.data.TODODao
import com.nyinnovations.todoapp.data.TODOItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TODORepository @Inject constructor(private val dao: TODODao) {

    fun getTODOs(): Flow<List<TODOItem>> {
        return dao.getAllTODOs()
    }

    suspend fun addTODO(todoItem: TODOItem) {
        dao.insertTODO(todoItem)
    }
}
