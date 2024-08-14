package com.nyinnovations.todoapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TODODao {

    @Query("SELECT * FROM todo_table")
    fun getAllTODOs(): Flow<List<TODOItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTODO(todoItem: TODOItem)
}
