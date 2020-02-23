package com.arctouch.codechallenge.datasource

import com.arctouch.codechallenge.model.MoviesResponse
import kotlinx.coroutines.CoroutineScope

class UpcomingMovieListDataSource(
        coroutineScope: CoroutineScope
) : MovieListDataSource(coroutineScope) {

    override suspend fun loadInitialFromService(): MoviesResponse = movieRepository.getUpcomingMovieList(1L)

    override suspend fun loadPageFromService(currentPageKey: Long): MoviesResponse = movieRepository.getUpcomingMovieList(currentPageKey)
}