package com.example.kotlincoroutines.presenter

import com.example.kotlincoroutines.repository.MoviesRepository

class MoviesPresenter(private val repository: MoviesRepository)
    : BasePresenter<MoviesContract.View>(), MoviesContract.Presenter {

    override fun getMovies() {
        repository.getMovies(
            onSuccess = { movies ->
                getView()?.showMovies(movies)
            },
            onError = { throwable ->
                getView()?.showError(throwable)
            }
        )
    }
}