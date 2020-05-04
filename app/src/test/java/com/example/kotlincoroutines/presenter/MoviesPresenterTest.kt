package com.example.kotlincoroutines.presenter

import com.example.kotlincoroutines.data.model.Movie
import com.example.kotlincoroutines.data.model.Result
import com.example.kotlincoroutines.repository.MoviesRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.lang.RuntimeException

@ExperimentalCoroutinesApi
class MoviesPresenterTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val testCoroutineScope = TestCoroutineScope(testDispatcher)

    private lateinit var presenter: MoviesPresenter
    @MockK lateinit var repository: MoviesRepository
    @MockK lateinit var view: MoviesContract.View

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)

        presenter = MoviesPresenter(repository).apply {
            setView(view)
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun getMoviesShowsResult() = testCoroutineScope.runBlockingTest {
        val movie = Movie(
            id = "419704",
            title = "Ad Astra",
            posterPath = "/xBHvZcjRiWyobQ9kxBhO6B2dtRI.jpg"
        )
        coEvery { repository.getMovies() } returns Result(listOf(movie), null)

        presenter.getMovies()
        advanceTimeBy(500)

        coVerifyOrder {
            repository.getMovies()
            view.showMovies(listOf(movie))
        }
    }

    @Test
    fun getMoviesShowsError() = testCoroutineScope.runBlockingTest {
        val throwable = RuntimeException()
        coEvery { repository.getMovies() } returns Result(null, throwable)

        presenter.getMovies()
        advanceTimeBy(500)

        coVerifyOrder {
            repository.getMovies()
            view.showError(throwable)
        }
    }
}