package com.arctouch.codechallenge.datasource

import com.arctouch.codechallenge.model.Movie
import kotlinx.coroutines.CoroutineScope

class UpcomingMovieListDataSource(
        coroutineScope: CoroutineScope
) : MovieListDataSource(coroutineScope) {

    override suspend fun loadInitialFromService(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Long, Movie>) {
        val (currentPage, movieList, _, movieCount) = movieRepository.getUpcomingMovieList(1L)
        val currentPosition = if (movieList.size == movieCount) 0 else currentPage
        callback.onResult(movieList, currentPosition, movieCount, currentPage - 1L, currentPage + 1L)
    }

    override suspend fun loadPageFromService(currentPageKey: Long, adjacentPageKey: Long, callback: LoadCallback<Long, Movie>) {
        val movieList = movieRepository.getUpcomingMovieList(currentPageKey).results
        callback.onResult(movieList, adjacentPageKey)
    }
}