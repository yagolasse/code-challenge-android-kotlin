package com.arctouch.codechallenge.repository

import com.arctouch.codechallenge.constants.Constants.API_KEY
import com.arctouch.codechallenge.constants.Constants.DEFAULT_LANGUAGE
import com.arctouch.codechallenge.constants.Constants.DEFAULT_REGION
import com.arctouch.codechallenge.dao.MovieDao
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.model.MoviesResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.KoinComponent
import org.koin.core.inject

class MovieRepositoryImpl : KoinComponent, MovieRepository {

    private val genreRepository by inject<GenreRepository>()
    private val movieDao by inject<MovieDao>()

    override fun getMovie(id: Long): Flow<Movie> = flow {
        emit(movieDao.movie(id, API_KEY, DEFAULT_LANGUAGE))
    }

    override suspend fun getUpcomingMovieList(page: Long): MoviesResponse {
        val upcomingMoviesResponse = movieDao.upcomingMovies(API_KEY, DEFAULT_LANGUAGE, page, DEFAULT_REGION)
        return upcomingMoviesResponse.copy(results = bindGenresToMovies(upcomingMoviesResponse.results))
    }

    override suspend fun moviesByQuery(page: Long, query: String): MoviesResponse {
        val upcomingMoviesResponse = movieDao.moviesByQuery(API_KEY, DEFAULT_LANGUAGE, page, query, DEFAULT_REGION)
        return upcomingMoviesResponse.copy(results = bindGenresToMovies(upcomingMoviesResponse.results))
    }

    private suspend fun bindGenresToMovies(movies: List<Movie>): List<Movie> {
        val genres = genreRepository.getGenreList()

        return movies.map { movie ->
            movie.copy(genres = genres.filter { movie.genreIds?.contains(it.id) == true })
        }
    }
}