package com.example.kotlincoroutines.repository

import com.example.kotlincoroutines.app.coroutineLog
import com.example.kotlincoroutines.contextprovider.CoroutineContextProvider
import com.example.kotlincoroutines.data.database.MovieDao
import com.example.kotlincoroutines.data.model.Movie
import com.example.kotlincoroutines.data.model.Result
import com.example.kotlincoroutines.data.network.MoviesService
import com.example.kotlincoroutines.di.API_KEY
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException

class MoviesRepositoryImpl(
    private val moviesService: MoviesService,
    private val movieDao: MovieDao,
    private val contextProvider: CoroutineContextProvider
) : MoviesRepository {

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun getMovies(): Result<List<Movie>> = withContext(contextProvider.context) {
        supervisorScope {
            Timber.d(coroutineLog)

            val savedMoviesDeferred = async {
                Timber.d(coroutineLog)
                movieDao.getMovies()
            }
            val responseDeferred = async {
                Timber.d(coroutineLog)
                moviesService.getMovies(API_KEY).execute()
            }

            val savedMovies = savedMoviesDeferred.await()
            try {
                val response = responseDeferred.await()
                val movies = response.body()?.movies?.also {
                    launch {
                        Timber.d(coroutineLog)
                        movieDao.saveMovies(it)
                    }
                }

                if (response.isSuccessful && movies != null) {
                    Result(movies, null)
                } else {
                    Result(savedMovies, null)
                }
            } catch (throwable: Throwable) {
                if (throwable is IOException && savedMovies.isNotEmpty()) {
                    Result(savedMovies, null)
                } else {
                    Result(null, throwable)
                }
            }
        }
    }
}