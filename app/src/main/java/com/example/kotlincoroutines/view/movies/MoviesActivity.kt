package com.example.kotlincoroutines.view.movies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlincoroutines.R
import com.example.kotlincoroutines.app.exhaustive
import com.example.kotlincoroutines.app.toast
import com.example.kotlincoroutines.data.model.Movie
import com.example.kotlincoroutines.data.model.Result
import com.example.kotlincoroutines.databinding.ActivityMoviesBinding
import com.example.kotlincoroutines.viewmodel.MoviesViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class MoviesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMoviesBinding

    private val viewModel: MoviesViewModel by viewModel()
    private val adapter = MoviesAdapter(mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setMoviesRecyclerView()
        setMoviesLiveDataObserver()
    }

    private fun showMovies(movies: List<Movie>) {
        adapter.updateMovies(movies)

        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun showError(throwable: Throwable) {
        toast(R.string.cant_load_movies)
        Timber.e(throwable.localizedMessage)

        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun setMoviesRecyclerView() {
        binding.moviesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.moviesRecyclerView.adapter = adapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getMovies()
        }
    }

    private fun setMoviesLiveDataObserver() {
        viewModel.moviesLiveData.observe(this, Observer { result ->
            when (result) {
                is Result.Success -> {
                    showMovies(result.value)
                }
                is Result.Error -> {
                    showError(result.throwable)
                }
            }.exhaustive
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel.getMovies()
    }
}