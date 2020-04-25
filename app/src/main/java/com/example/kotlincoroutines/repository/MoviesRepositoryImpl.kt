package com.example.kotlincoroutines.repository

import com.example.kotlincoroutines.data.database.MovieDao
import com.example.kotlincoroutines.data.model.Movie
import com.example.kotlincoroutines.data.model.MoviesResponse
import com.example.kotlincoroutines.data.network.MoviesService
import com.example.kotlincoroutines.di.API_KEY
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class MoviesRepositoryImpl(
    private val moviesService: MoviesService,
    private val movieDao: MovieDao
) : MoviesRepository {

    override fun getMovies(
        onSuccess: (List<Movie>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        moviesService.getMovies(API_KEY).enqueue(object : Callback<MoviesResponse> {
            override fun onResponse(call: Call<MoviesResponse>, response: Response<MoviesResponse>) {
                val movies = response.body()?.movies ?: emptyList()

                if (movies.isNotEmpty()) {
                    movieDao.saveMovies(movies)
                }

                onSuccess(movies)
            }

            override fun onFailure(call: Call<MoviesResponse>, throwable: Throwable) {
                val savedMovies = movieDao.getMovies()

                if (throwable is IOException && savedMovies.isNotEmpty()) {
                    onSuccess(savedMovies)
                } else {
                    onError(throwable)
                }
            }
        })
    }
}