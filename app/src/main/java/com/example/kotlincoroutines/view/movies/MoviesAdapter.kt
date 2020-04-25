package com.example.kotlincoroutines.view.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlincoroutines.data.model.Movie
import com.example.kotlincoroutines.databinding.ListItemMovieBinding
import com.example.kotlincoroutines.di.MOVIE_IMAGE_BASE_PATH

class MoviesAdapter(private val movies: MutableList<Movie>)
    : RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    fun updateMovies(movies: List<Movie>) {
        this.movies.clear()
        this.movies.addAll(movies)
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ListItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {

        private val movieImage = binding.movieImage

        fun bind(movie: Movie) {
            Glide.with(itemView.context)
                .load(MOVIE_IMAGE_BASE_PATH + movie.posterPath)
                .centerCrop()
                .into(movieImage)
        }
    }
}