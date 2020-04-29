package com.example.kotlincoroutines.repository

import com.example.kotlincoroutines.contextprovider.CoroutineContextProvider
import com.example.kotlincoroutines.data.database.MovieDao
import com.example.kotlincoroutines.data.model.Movie
import com.example.kotlincoroutines.data.model.Result
import com.example.kotlincoroutines.data.network.MoviesService
import com.example.kotlincoroutines.di.API_KEY
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class MoviesRepositoryImpl(
    private val moviesService: MoviesService,
    private val movieDao: MovieDao,
    private val contextProvider: CoroutineContextProvider
) : MoviesRepository {

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun getMovies(): Result<List<Movie>> = withContext(contextProvider.context) {
        val savedMoviesDeferred = async { movieDao.getMovies() }

        try {
            val response = moviesService.getMovies(API_KEY).execute()
            val movies = response.body()?.movies?.also {
                launch { movieDao.saveMovies(it) }
            }

            if (response.isSuccessful && movies != null) {
                Result(movies, null)
            } else {
                Result(savedMoviesDeferred.await(), null)
            }
        } catch (throwable: Throwable) {
            val savedMovie = savedMoviesDeferred.await()

            if (throwable is IOException && savedMovie.isNotEmpty()) {
                Result(savedMovie, null)
            } else {
                Result(null, throwable)
            }
        }
    }
}