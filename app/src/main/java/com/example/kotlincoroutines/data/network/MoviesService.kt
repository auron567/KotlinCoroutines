package com.example.kotlincoroutines.data.network

import com.example.kotlincoroutines.data.model.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesService {

    @GET("3/movie/popular")
    suspend fun getMovies(
        @Query("api_key") apiKey: String
    ): MoviesResponse
}