package com.example.kotlincoroutines.di

import com.example.kotlincoroutines.contextprovider.CoroutineContextProvider
import com.example.kotlincoroutines.contextprovider.CoroutineContextProviderImpl
import com.example.kotlincoroutines.data.database.MovieDao
import com.example.kotlincoroutines.data.network.MoviesService
import com.example.kotlincoroutines.repository.MoviesRepository
import com.example.kotlincoroutines.repository.MoviesRepositoryImpl
import com.example.kotlincoroutines.viewmodel.MoviesViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

val appModule = module {

    // CoroutineContextProvider instance
    single<CoroutineContextProvider> { provideCoroutineContextProviderImpl(Dispatchers.IO) }
    // MoviesRepository instance
    single<MoviesRepository> { provideMovieRepositoryImpl(get(), get()) }
    // MoviesViewModel instance
    viewModel { provideMoviesViewModel(get()) }
}

private fun provideCoroutineContextProviderImpl(context: CoroutineContext): CoroutineContextProviderImpl {
    return CoroutineContextProviderImpl(context)
}

private fun provideMovieRepositoryImpl(moviesService: MoviesService, movieDao: MovieDao): MoviesRepositoryImpl {
    return MoviesRepositoryImpl(moviesService, movieDao)
}

private fun provideMoviesViewModel(repository: MoviesRepository): MoviesViewModel {
    return MoviesViewModel(repository)
}