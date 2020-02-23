package com.arctouch.codechallenge.datasource

import com.arctouch.codechallenge.model.MoviesResponse
import kotlinx.coroutines.CoroutineScope

class MovieByQueryListDataSource(
        private val query: String,
        coroutineScope: CoroutineScope
) : MovieListDataSource(coroutineScope) {

    override suspend fun loadInitialFromService(): MoviesResponse = movieRepository.moviesByQuery(1L, query)

    override suspend fun loadPageFromService(currentPageKey: Long): MoviesResponse = movieRepository.moviesByQuery(currentPageKey, query)
}