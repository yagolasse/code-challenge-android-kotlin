package com.arctouch.codechallenge.repository

import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.model.MoviesResponse
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getUpcomingMovieList(page: Long): Flow<MoviesResponse>
    fun getMovie(id: Long): Flow<Movie>
    suspend fun moviesByQuery(page: Long, query: String): MoviesResponse
}