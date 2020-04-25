package com.example.kotlincoroutines.di

import android.app.Application
import androidx.room.Room
import com.example.kotlincoroutines.data.database.MovieDao
import com.example.kotlincoroutines.data.database.MovieDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {

    // MovieDatabase instance
    single { provideMovieDatabase(androidApplication()) }
    // MovieDao instance
    single { provideMovieDao(get()) }
}

private fun provideMovieDatabase(application: Application): MovieDatabase {
    return Room.databaseBuilder(application, MovieDatabase::class.java, "movie_database")
        .allowMainThreadQueries()
        .build()
}

private fun provideMovieDao(movieDatabase: MovieDatabase): MovieDao {
    return movieDatabase.movieDao()
}