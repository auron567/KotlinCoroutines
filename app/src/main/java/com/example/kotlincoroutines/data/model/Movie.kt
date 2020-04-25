package com.example.kotlincoroutines.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movie_table")
data class Movie(
    @PrimaryKey val id: String,
    val title: String,
    @SerializedName("poster_path") val posterPath: String
)