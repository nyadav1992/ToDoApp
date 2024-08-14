package com.nyinnovations.todoapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
data class TODOItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String
)