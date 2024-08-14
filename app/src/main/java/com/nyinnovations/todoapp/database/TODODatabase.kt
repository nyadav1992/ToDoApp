package com.nyinnovations.todoapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nyinnovations.todoapp.data.TODODao
import com.nyinnovations.todoapp.data.TODOItem

@Database(entities = [TODOItem::class], version = 1)
abstract class TODODatabase : RoomDatabase() {
    abstract fun todoDao(): TODODao
}
