package com.example.kotlincoroutines.repository

import com.example.kotlincoroutines.data.model.Movie
import com.example.kotlincoroutines.data.model.Result

interface MoviesRepository {

    suspend fun getMovies(): Result<List<Movie>>
}