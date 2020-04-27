package com.example.kotlincoroutines.presenter

import com.example.kotlincoroutines.repository.MoviesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MoviesPresenter(private val repository: MoviesRepository)
    : BasePresenter<MoviesContract.View>(), MoviesContract.Presenter, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override fun getMovies() {
        launch {
            val result = repository.getMovies()

            if (result.value != null && result.value.isNotEmpty()) {
                getView()?.showMovies(result.value)
            } else if (result.throwable != null) {
                getView()?.showError(result.throwable)
            }
        }
    }
}