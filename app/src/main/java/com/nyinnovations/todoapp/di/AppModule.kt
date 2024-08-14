package com.nyinnovations.todoapp.di

import android.content.Context
import androidx.room.Room
import com.nyinnovations.todoapp.data.TODODao
import com.nyinnovations.todoapp.database.TODODatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): TODODatabase {
        return Room.databaseBuilder(
            appContext,
            TODODatabase::class.java,
            "todo_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTODODao(db: TODODatabase): TODODao {
        return db.todoDao()
    }
}