package com.arctouch.codechallenge.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.model.MoviesResponse
import com.arctouch.codechallenge.repository.MovieRepository
import com.arctouch.codechallenge.util.SingleLiveEvent
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import org.koin.core.inject

abstract class MovieListDataSource(
        private val scope: CoroutineScope
) : PageKeyedDataSource<Long, Movie>(), KoinComponent {

    protected val movieRepository by inject<MovieRepository>()

    private val _initialLoadingLiveData = MutableLiveData<Boolean>(false)
    val initialLoadingLiveData: LiveData<Boolean> get() = _initialLoadingLiveData

    private val _pageRequestLoadingLiveData = MutableLiveData<Boolean>(false)
    val pageRequestLoadingLiveData: LiveData<Boolean> get() = _pageRequestLoadingLiveData

    private val _listIsEmptyLiveData = MutableLiveData<Boolean>(false)
    val listIsEmptyLiveData: LiveData<Boolean> = _listIsEmptyLiveData

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

    abstract suspend fun loadInitialFromService(): MoviesResponse
    abstract suspend fun loadPageFromService(currentPageKey: Long): MoviesResponse

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Long, Movie>) {
        scope.launch(Dispatchers.Main + initialLoadCoroutineExceptionHandler) {
            _initialLoadingLiveData.value = true
            withContext(Dispatchers.IO + initialLoadCoroutineExceptionHandler) {
                val (currentPage, movieList, _, movieCount) = loadInitialFromService()
                val currentPosition = if (movieList.size == movieCount) 0 else currentPage
                _listIsEmptyLiveData.postValue(movieList.isEmpty())
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
        scope.launch(Dispatchers.Main + pageLoadCoroutineExceptionHandler) {
            _pageRequestLoadingLiveData.value = true
            withContext(Dispatchers.IO + pageLoadCoroutineExceptionHandler) {
                val movieList = loadPageFromService(currentPageKey).results
                callback.onResult(movieList, adjacentPageKey)
            }
            _pageRequestLoadingLiveData.value = false
        }
    }
}