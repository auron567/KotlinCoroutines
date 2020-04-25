package com.example.kotlincoroutines.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kotlincoroutines.data.model.Movie

@Database(entities = [Movie::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
}