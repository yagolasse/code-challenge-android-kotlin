package com.arctouch.codechallenge.repository

import com.arctouch.codechallenge.constants.Constants.API_KEY
import com.arctouch.codechallenge.constants.Constants.DEFAULT_LANGUAGE
import com.arctouch.codechallenge.constants.Constants.DEFAULT_REGION
import com.arctouch.codechallenge.dao.MovieDao
import com.arctouch.codechallenge.model.UpcomingMoviesResponse
import org.koin.core.KoinComponent
import org.koin.core.inject

class MovieRepositoryImpl : KoinComponent, MovieRepository {

    private val genreRepository by inject<GenreRepository>()
    private val movieDao by inject<MovieDao>()

    override suspend fun getMovieList(page: Long): UpcomingMoviesResponse {
        val upcomingMoviesResponse = movieDao.upcomingMovies(
                API_KEY,
                DEFAULT_LANGUAGE,
                page,
                DEFAULT_REGION
        )

        val genres = genreRepository.getGenreList()

        val moviesWithGenres = upcomingMoviesResponse.results.map { movie ->
            movie.copy(genres = genres.filter { movie.genreIds?.contains(it.id) == true })
        }

        return upcomingMoviesResponse.copy(results = moviesWithGenres)
    }
}