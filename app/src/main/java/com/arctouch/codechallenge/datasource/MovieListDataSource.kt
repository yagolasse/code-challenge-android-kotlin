package com.arctouch.codechallenge.datasource

import androidx.paging.PageKeyedDataSource
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class MovieListDataSource(
        private val scope: CoroutineScope
) : PageKeyedDataSource<Long, Movie>(), KoinComponent {

    private val movieRepository by inject<MovieRepository>()

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Long, Movie>) {
        scope.launch {
            val (currentPage, movieList, _, movieCount) = movieRepository.getMovieList(1L)
            callback.onResult(movieList, currentPage, movieCount, currentPage - 1L, currentPage + 1L)
        }
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, Movie>) {
        scope.launch {
            val movieList = movieRepository.getMovieList(params.key).results
            callback.onResult(movieList, params.key + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, Movie>) {
        // Does not load if it is in the first page
        if (params.key <= 1L) return

        scope.launch {
            val movieList = movieRepository.getMovieList(params.key).results
            callback.onResult(movieList, params.key - 1)
        }
    }

    override fun invalidate() {
        super.invalidate()
        scope.cancel()
    }
}