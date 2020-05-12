package com.example.kotlincoroutines.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.kotlincoroutines.utils.MovieDataFactory
import com.example.kotlincoroutines.data.model.Result
import com.example.kotlincoroutines.repository.MoviesRepository
import com.example.kotlincoroutines.utils.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.lang.RuntimeException

@ExperimentalCoroutinesApi
class MoviesViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule: CoroutineTestRule = CoroutineTestRule()

    private lateinit var viewModel: MoviesViewModel
    @MockK lateinit var repository: MoviesRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = MoviesViewModel(repository)
    }

    @Test
    fun getMoviesCallsRepository() = coroutineTestRule.runBlockingTest {
        viewModel.getMovies()
        advanceTimeBy(500)

        coVerify(exactly = 1) {
            repository.getMovies()
        }
    }

    @Test
    fun getMoviesReturnsSuccess() = coroutineTestRule.runBlockingTest {
        val movies = MovieDataFactory.makeMovieList(20)
        coEvery { repository.getMovies() } returns Result.Success(movies)

        viewModel.getMovies()
        advanceTimeBy(500)

        assertTrue(viewModel.moviesLiveData.value is Result.Success)
    }

    @Test
    fun getMoviesReturnsError() = coroutineTestRule.runBlockingTest {
        val throwable = RuntimeException()
        coEvery { repository.getMovies() } returns Result.Error(throwable)

        viewModel.getMovies()
        advanceTimeBy(500)

        assertTrue(viewModel.moviesLiveData.value is Result.Error)
    }
}