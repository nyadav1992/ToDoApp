package com.nyinnovations.todo_datasource.data.repo

import com.nyinnovations.todo_datasource.data.TODODao
import com.nyinnovations.todo_datasource.data.TODOItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TODORepository @Inject constructor(private val dao: TODODao) {

    fun getTODOs(): Flow<List<TODOItem>> {
        return dao.getAllTODOs()
    }

    suspend fun addTODO(todoItem: TODOItem) {
        dao.insertTODO(todoItem)
    }

    suspend fun deleteTODO(todoItem: TODOItem) {
        dao.deleteTODO(todoItem)
    }
}
