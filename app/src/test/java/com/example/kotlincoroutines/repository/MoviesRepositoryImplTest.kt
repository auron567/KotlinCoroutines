package com.example.kotlincoroutines.repository

import com.example.kotlincoroutines.MovieDataFactory
import com.example.kotlincoroutines.data.database.MovieDao
import com.example.kotlincoroutines.data.model.Result
import com.example.kotlincoroutines.data.network.MoviesService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals
import java.io.IOException
import java.lang.RuntimeException

@ExperimentalCoroutinesApi
class MoviesRepositoryImplTest {

    private lateinit var repository: MoviesRepositoryImpl
    @MockK lateinit var service: MoviesService
    @RelaxedMockK lateinit var dao: MovieDao

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repository = MoviesRepositoryImpl(service, dao)
    }

    @Test
    fun getMoviesCallsService() = runBlockingTest {
        coEvery { service.getMovies(any()) } returns MovieDataFactory.makeMoviesResponse()

        repository.getMovies()

        coVerify(exactly = 1) {
            service.getMovies(any())
        }
    }

    @Test
    fun getMoviesReturnsSuccessData() = runBlockingTest {
        val response = MovieDataFactory.makeMoviesResponse()
        coEvery { service.getMovies(any()) } returns response

        val result = repository.getMovies()

        val expectedResult = Result.Success(response.movies)
        assertReflectionEquals(expectedResult, result)
    }

    @Test
    fun getMoviesSavesMoviesOnDatabase() = runBlockingTest {
        val response = MovieDataFactory.makeMoviesResponse()
        coEvery { service.getMovies(any()) } returns response

        repository.getMovies()

        coVerify(exactly = 1) {
            dao.saveMovies(response.movies)
        }
    }

    @Test
    fun getMoviesReturnsSavedMovies() = runBlockingTest {
        val savedMovies = MovieDataFactory.makeMovieList(20)
        coEvery { dao.getMovies() } returns savedMovies
        coEvery { service.getMovies(any()) } throws IOException()

        val result = repository.getMovies()

        val expectedResult = Result.Success(savedMovies)
        assertReflectionEquals(expectedResult, result)
    }

    @Test
    fun getMoviesReturnsErrorData() = runBlockingTest {
        val throwable = RuntimeException()
        coEvery { service.getMovies(any()) } throws throwable
        coEvery { dao.getMovies() } returns MovieDataFactory.makeMovieList(20)

        val result = repository.getMovies()

        val expectedResult = Result.Error(throwable)
        assertReflectionEquals(expectedResult, result)
    }
}