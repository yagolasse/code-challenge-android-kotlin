package com.arctouch.codechallenge.repository

import com.arctouch.codechallenge.model.UpcomingMoviesResponse

interface MovieRepository {
    suspend fun getMovieList(page: Long): UpcomingMoviesResponse
}