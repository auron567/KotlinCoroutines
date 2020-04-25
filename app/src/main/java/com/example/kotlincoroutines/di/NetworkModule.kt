package com.example.kotlincoroutines.di

import com.example.kotlincoroutines.data.network.MoviesService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.themoviedb.org/"
const val MOVIE_IMAGE_BASE_PATH = "https://image.tmdb.org/t/p/w500"
const val API_KEY = "INSERT-API-KEY-HERE"

val networkModule = module {

    // HttpLoggingInterceptor instance
    single { provideLoggingInterceptor() }
    // OkHttpClient instance
    single { provideOkHttpClient(get()) }
    // Retrofit instance
    single { provideRetrofit(get()) }
    // MovieService instance
    single { provideMovieService(get()) }
}

private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}

private fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
}

private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

private fun provideMovieService(retrofit: Retrofit): MoviesService {
    return retrofit.create(MoviesService::class.java)
}