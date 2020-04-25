package com.example.kotlincoroutines.di

import com.example.kotlincoroutines.data.database.MovieDao
import com.example.kotlincoroutines.data.network.MoviesService
import com.example.kotlincoroutines.presenter.MoviesPresenter
import com.example.kotlincoroutines.repository.MoviesRepository
import com.example.kotlincoroutines.repository.MoviesRepositoryImpl
import org.koin.dsl.module

val appModule = module {

    // MoviesRepository instance
    single<MoviesRepository> { provideMovieRepositoryImpl(get(), get()) }
    // MoviesPresenter instance
    factory { provideMoviesPresenter(get()) }
}

private fun provideMovieRepositoryImpl(movieService: MoviesService, movieDao: MovieDao): MoviesRepositoryImpl {
    return MoviesRepositoryImpl(movieService, movieDao)
}

private fun provideMoviesPresenter(repository: MoviesRepository): MoviesPresenter {
    return MoviesPresenter(repository)
}