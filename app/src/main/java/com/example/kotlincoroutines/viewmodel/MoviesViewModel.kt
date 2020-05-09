package com.example.kotlincoroutines.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlincoroutines.app.coroutineLog
import com.example.kotlincoroutines.data.model.Movie
import com.example.kotlincoroutines.data.model.Result
import com.example.kotlincoroutines.repository.MoviesRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class MoviesViewModel(private val repository: MoviesRepository) : ViewModel() {

    private val _moviesLiveData = MutableLiveData<Result<List<Movie>>>()
    val moviesLiveData: LiveData<Result<List<Movie>>>
        get() = _moviesLiveData

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable.localizedMessage)
    }

    fun getMovies() {
        viewModelScope.launch(coroutineExceptionHandler) {
            Timber.d(coroutineLog)

            delay(500)
            val result = repository.getMovies()

            Timber.d("Coroutine still alive!")
            _moviesLiveData.postValue(result)
        }
    }
}