package com.example.kotlincoroutines

import com.example.kotlincoroutines.data.model.Movie
import com.example.kotlincoroutines.data.model.MoviesResponse
import java.util.*

object MovieDataFactory {

    fun makeMoviesResponse(): MoviesResponse {
        val movies = makeMovieList(20)

        return MoviesResponse(movies)
    }

    fun makeMovieList(count: Int): List<Movie> {
        val movies = mutableListOf<Movie>()
        repeat(count) {
            movies.add(makeMovie())
        }

        return movies
    }

    private fun makeMovie() = Movie(
        id = UUID.randomUUID().toString(),
        title = UUID.randomUUID().toString(),
        posterPath = UUID.randomUUID().toString()
    )
}