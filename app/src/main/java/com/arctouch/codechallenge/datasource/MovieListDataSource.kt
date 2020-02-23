package com.arctouch.codechallenge.datasource

import androidx.paging.PageKeyedDataSource
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class MovieListDataSource(
        private val scope: CoroutineScope,
        private val query: String?
) : PageKeyedDataSource<Long, Movie>(), KoinComponent {

    private val movieRepository by inject<MovieRepository>()

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Long, Movie>) {
        if (query?.isEmpty() == true) return

        scope.launch(Dispatchers.IO) {
            val (currentPage, movieList, _, movieCount) = if (query == null) movieRepository.getUpcomingMovieList(1L)
            else movieRepository.moviesByQuery(1L, query)
            val currentPosition = if (movieList.size == movieCount) 0 else currentPage
            callback.onResult(movieList, currentPosition, movieCount, currentPage - 1L, currentPage + 1L)
        }
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, Movie>) {
        executeAdjacentPageCall(params.key, params.key + 1, callback)
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, Movie>) {
        // Does not load if it is in the first page
        if (params.key <= 1L) return

        executeAdjacentPageCall(params.key, params.key - 1, callback)
    }

    private fun executeAdjacentPageCall(currentPageKey: Long, adjacentPageKey: Long, callback: LoadCallback<Long, Movie>) {
        if (query?.isEmpty() == true) return

        scope.launch(Dispatchers.IO) {
            val movieList = if (query == null) movieRepository.getUpcomingMovieList(currentPageKey).results
            else movieRepository.moviesByQuery(currentPageKey, query).results
            callback.onResult(movieList, adjacentPageKey)
        }
    }
}