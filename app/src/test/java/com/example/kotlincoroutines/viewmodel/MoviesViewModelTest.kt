package com.example.kotlincoroutines.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.kotlincoroutines.MovieDataFactory
import com.example.kotlincoroutines.data.model.Result
import com.example.kotlincoroutines.repository.MoviesRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.lang.RuntimeException

@ExperimentalCoroutinesApi
class MoviesViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val testCoroutineScope = TestCoroutineScope(testDispatcher)

    private lateinit var viewModel: MoviesViewModel
    @MockK lateinit var repository: MoviesRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)

        viewModel = MoviesViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun getMoviesCallsRepository() = testCoroutineScope.runBlockingTest {
        viewModel.getMovies()
        advanceTimeBy(500)

        coVerify(exactly = 1) {
            repository.getMovies()
        }
    }

    @Test
    fun getMoviesReturnsSuccess() = testCoroutineScope.runBlockingTest {
        val movies = MovieDataFactory.makeMovieList(20)
        coEvery { repository.getMovies() } returns Result.Success(movies)

        viewModel.getMovies()
        advanceTimeBy(500)

        assertTrue(viewModel.moviesLiveData.value is Result.Success)
    }

    @Test
    fun getMoviesReturnsError() = testCoroutineScope.runBlockingTest {
        val throwable = RuntimeException()
        coEvery { repository.getMovies() } returns Result.Error(throwable)

        viewModel.getMovies()
        advanceTimeBy(500)

        assertTrue(viewModel.moviesLiveData.value is Result.Error)
    }
}