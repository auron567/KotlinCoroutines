package com.example.kotlincoroutines.data.model

import com.google.gson.annotations.SerializedName

class MoviesResponse(
    @SerializedName("results") val movies: List<Movie>
)