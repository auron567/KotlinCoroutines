package com.example.kotlincoroutines.repository

import com.example.kotlincoroutines.data.database.MovieDao
import com.example.kotlincoroutines.data.model.Movie
import com.example.kotlincoroutines.data.model.Result
import com.example.kotlincoroutines.data.network.MoviesService
import com.example.kotlincoroutines.di.API_KEY
import java.io.IOException

class MoviesRepositoryImpl(
    private val moviesService: MoviesService,
    private val movieDao: MovieDao
) : MoviesRepository {

    override suspend fun getMovies(): Result<List<Movie>> {
        return try {
            val response = moviesService.getMovies(API_KEY)
            val movies = response.movies.also {
                movieDao.saveMovies(it)
            }

            Result.Success(movies)
        } catch (throwable: Throwable) {
            val savedMovies = movieDao.getMovies()

            if (throwable is IOException && savedMovies.isNotEmpty()) {
                Result.Success(savedMovies)
            } else {
                Result.Error(throwable)
            }
        }
    }
}