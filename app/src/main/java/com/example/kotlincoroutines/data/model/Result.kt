package com.example.kotlincoroutines.data.model

sealed class Result<out T : Any> {

    class Success<out T : Any>(val value: T) : Result<T>()

    class Error(val throwable: Throwable) : Result<Nothing>()
}