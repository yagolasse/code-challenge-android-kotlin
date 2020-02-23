package com.arctouch.codechallenge.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.repository.MovieRepository
import com.arctouch.codechallenge.util.SingleLiveEvent
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import org.koin.core.inject

class MovieListDataSource(
        private val scope: CoroutineScope,
        private val query: String?
) : PageKeyedDataSource<Long, Movie>(), KoinComponent {

    private val movieRepository by inject<MovieRepository>()

    private val _initialLoadingLiveData = MutableLiveData<Boolean>()
    val initialLoadingLiveData: LiveData<Boolean> get() = _initialLoadingLiveData

    private val _pageRequestLoadingLiveData = MutableLiveData<Boolean>()
    val pageRequestLoadingLiveData: LiveData<Boolean> get() = _pageRequestLoadingLiveData

    private val initialLoadCoroutineExceptionHandler = CoroutineExceptionHandler { _, _ ->
        initialLoadErrorEvent.call()
        _initialLoadingLiveData.value = false
    }

    private val pageLoadCoroutineExceptionHandler = CoroutineExceptionHandler { _, _ ->
        pageLoadErrorEvent.call()
        _pageRequestLoadingLiveData.value = false
    }

    val initialLoadErrorEvent = SingleLiveEvent<Unit>()
    val pageLoadErrorEvent = SingleLiveEvent<Unit>()

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Long, Movie>) {
        if (query?.isEmpty() == true) {
            _initialLoadingLiveData.postValue(false)
            return
        }

        scope.launch(Dispatchers.Main + initialLoadCoroutineExceptionHandler) {
            _initialLoadingLiveData.value = true
            withContext(Dispatchers.IO + initialLoadCoroutineExceptionHandler) {
                val (currentPage, movieList, _, movieCount) = if (query == null) movieRepository.getUpcomingMovieList(1L)
                else movieRepository.moviesByQuery(1L, query)
                val currentPosition = if (movieList.size == movieCount) 0 else currentPage
                callback.onResult(movieList, currentPosition, movieCount, currentPage - 1L, currentPage + 1L)
            }
            _initialLoadingLiveData.value = false
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
        if (query?.isEmpty() == true) {
            _initialLoadingLiveData.postValue(false)
            return
        }

        scope.launch(Dispatchers.Main + pageLoadCoroutineExceptionHandler) {
            _pageRequestLoadingLiveData.value = true
            withContext(Dispatchers.IO + pageLoadCoroutineExceptionHandler) {
                val movieList = if (query == null) movieRepository.getUpcomingMovieList(currentPageKey).results
                else movieRepository.moviesByQuery(currentPageKey, query).results
                callback.onResult(movieList, adjacentPageKey)
            }
            _pageRequestLoadingLiveData.value = false
        }
    }
}