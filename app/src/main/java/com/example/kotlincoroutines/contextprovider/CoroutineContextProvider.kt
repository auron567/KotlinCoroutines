package com.example.kotlincoroutines.contextprovider

import kotlin.coroutines.CoroutineContext

interface CoroutineContextProvider {

    val context: CoroutineContext
}