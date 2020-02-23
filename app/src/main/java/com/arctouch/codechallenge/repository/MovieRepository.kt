package com.arctouch.codechallenge.repository

import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.model.MoviesResponse

interface MovieRepository {
    suspend fun getUpcomingMovieList(page: Long): MoviesResponse
    suspend fun getMovie(id: Long): Movie
    suspend fun moviesByQuery(page: Long, query: String): MoviesResponse
}