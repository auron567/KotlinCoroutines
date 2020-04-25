package com.example.kotlincoroutines.repository

import com.example.kotlincoroutines.data.model.Movie

interface MoviesRepository {

    fun getMovies(
        onSuccess: (List<Movie>) -> Unit,
        onError: (Throwable) -> Unit
    )
}