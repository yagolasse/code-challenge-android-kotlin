package com.arctouch.codechallenge.repository

import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.dao.MovieDao
import com.arctouch.codechallenge.model.Movie
import org.koin.core.KoinComponent
import org.koin.core.inject

class MovieRepositoryImpl : KoinComponent, MovieRepository {

    private val genreRepository by inject<GenreRepository>()
    private val movieDao by inject<MovieDao>()

    override suspend fun getMovieList(page: Long): List<Movie> {
        val movies = movieDao.upcomingMovies(
                TmdbApi.API_KEY,
                TmdbApi.DEFAULT_LANGUAGE,
                page,
                TmdbApi.DEFAULT_REGION
        ).results

        val genres = genreRepository.getGenreList()

        return movies.map { movie ->
            movie.copy(genres = genres.filter { movie.genreIds?.contains(it.id) == true })
        }
    }
}