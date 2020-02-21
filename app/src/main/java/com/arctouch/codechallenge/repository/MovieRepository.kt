package com.arctouch.codechallenge.repository

import com.arctouch.codechallenge.model.Movie

interface MovieRepository {
    suspend fun getMovieList(page: Long): List<Movie>
}