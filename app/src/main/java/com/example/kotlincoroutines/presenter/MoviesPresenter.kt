package com.example.kotlincoroutines.presenter

import com.example.kotlincoroutines.app.coroutineLog
import com.example.kotlincoroutines.repository.MoviesRepository
import kotlinx.coroutines.*
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class MoviesPresenter(private val repository: MoviesRepository)
    : BasePresenter<MoviesContract.View>(), MoviesContract.Presenter, CoroutineScope {

    private val parentJob = SupervisorJob()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable.localizedMessage)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + parentJob + coroutineExceptionHandler

    override fun getMovies() {
        launch {
            Timber.d(coroutineLog)

            delay(500)
            val result = repository.getMovies()

            Timber.d("Coroutine still alive!")
            if (result.value != null && result.value.isNotEmpty()) {
                getView()?.showMovies(result.value)
            } else if (result.throwable != null) {
                getView()?.showError(result.throwable)
            }
        }
    }

    override fun stop() {
        parentJob.cancelChildren()
    }
}