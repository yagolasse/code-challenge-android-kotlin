package com.arctouch.codechallenge.repository

import com.arctouch.codechallenge.constants.Constants.API_KEY
import com.arctouch.codechallenge.constants.Constants.DEFAULT_LANGUAGE
import com.arctouch.codechallenge.constants.Constants.DEFAULT_REGION
import com.arctouch.codechallenge.dao.MovieDao
import com.arctouch.codechallenge.model.Genre
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.model.MoviesResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.zip
import org.koin.core.KoinComponent
import org.koin.core.inject

@ExperimentalCoroutinesApi
class MovieRepositoryImpl : KoinComponent, MovieRepository {

    private val genreRepository by inject<GenreRepository>()
    private val movieDao by inject<MovieDao>()

    override fun getMovie(id: Long): Flow<Movie> = flow {
        emit(movieDao.movie(id, API_KEY, DEFAULT_LANGUAGE))
    }

//    override suspend fun getUpcomingMovieList(page: Long): MoviesResponse {
//        val upcomingMoviesResponse = movieDao.upcomingMovies(API_KEY, DEFAULT_LANGUAGE, page, DEFAULT_REGION)
//        return upcomingMoviesResponse.copy(results = bindGenresToMovies(upcomingMoviesResponse.results))
//    }

    override suspend fun getUpcomingMovieList(page: Long): Flow<MoviesResponse> = flow {
        emit(movieDao.upcomingMovies(API_KEY, DEFAULT_LANGUAGE, page, DEFAULT_REGION))
    }.zip(genreRepository.getGenreList()) { movieResponse, genreList ->
        movieResponse.copy(results = bindGenresToMovies(movieResponse.results, genreList))
    }

    override suspend fun moviesByQuery(page: Long, query: String): MoviesResponse {
        val upcomingMoviesResponse = movieDao.moviesByQuery(API_KEY, DEFAULT_LANGUAGE, page, query, DEFAULT_REGION)
        return upcomingMoviesResponse.copy(results = bindGenresToMovies(upcomingMoviesResponse.results, emptyList()))
    }

    private fun bindGenresToMovies(movies: List<Movie>, genres: List<Genre>): List<Movie> = movies.map { movie ->
        movie.copy(genres = genres.filter { movie.genreIds?.contains(it.id) == true })
    }
}