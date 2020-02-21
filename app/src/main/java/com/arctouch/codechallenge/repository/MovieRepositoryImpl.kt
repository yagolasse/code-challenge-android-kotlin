package com.arctouch.codechallenge.repository

import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.dao.MovieDao
import com.arctouch.codechallenge.model.UpcomingMoviesResponse
import org.koin.core.KoinComponent
import org.koin.core.inject

class MovieRepositoryImpl : KoinComponent, MovieRepository {

    private val genreRepository by inject<GenreRepository>()
    private val movieDao by inject<MovieDao>()

    override suspend fun getMovieList(page: Long): UpcomingMoviesResponse {
        val upcomingMoviesResponse = movieDao.upcomingMovies(
                TmdbApi.API_KEY,
                TmdbApi.DEFAULT_LANGUAGE,
                page,
                TmdbApi.DEFAULT_REGION
        )

        val genres = genreRepository.getGenreList()

        val moviesWithGenres = upcomingMoviesResponse.results.map { movie ->
            movie.copy(genres = genres.filter { movie.genreIds?.contains(it.id) == true })
        }

        return upcomingMoviesResponse.copy(results = moviesWithGenres)
    }
}