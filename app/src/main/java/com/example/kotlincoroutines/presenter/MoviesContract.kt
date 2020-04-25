package com.example.kotlincoroutines.presenter

import com.example.kotlincoroutines.data.model.Movie

interface MoviesContract {

    interface Presenter {

        fun getMovies()
    }

    interface View {

        fun showMovies(movies: List<Movie>)

        fun showError(throwable: Throwable)
    }
}