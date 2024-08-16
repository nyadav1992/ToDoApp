package com.nyinnovations.todo_datasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nyinnovations.todo_datasource.data.TODODao
import com.nyinnovations.todo_datasource.data.TODOItem

@Database(entities = [TODOItem::class], version = 1)
abstract class TODODatabase : RoomDatabase() {
    abstract fun todoDao(): TODODao
}
