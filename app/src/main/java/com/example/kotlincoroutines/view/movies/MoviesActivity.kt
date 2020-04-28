package com.example.kotlincoroutines.view.movies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlincoroutines.R
import com.example.kotlincoroutines.app.toast
import com.example.kotlincoroutines.data.model.Movie
import com.example.kotlincoroutines.databinding.ActivityMoviesBinding
import com.example.kotlincoroutines.presenter.MoviesContract
import com.example.kotlincoroutines.presenter.MoviesPresenter
import org.koin.android.ext.android.inject
import timber.log.Timber

class MoviesActivity : AppCompatActivity(), MoviesContract.View {

    private lateinit var binding: ActivityMoviesBinding

    private val presenter: MoviesPresenter by inject()
    private val adapter = MoviesAdapter(mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setMoviesRecyclerView()

        presenter.setView(this)
    }

    override fun showMovies(movies: List<Movie>) {
        adapter.updateMovies(movies)

        binding.swipeRefreshLayout.isRefreshing = false
    }

    override fun showError(throwable: Throwable) {
        toast(R.string.cant_load_movies)
        Timber.e(throwable.localizedMessage)

        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun setMoviesRecyclerView() {
        binding.moviesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.moviesRecyclerView.adapter = adapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            presenter.getMovies()
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.getMovies()
    }

    override fun onStop() {
        presenter.stop()
        super.onStop()
    }
}