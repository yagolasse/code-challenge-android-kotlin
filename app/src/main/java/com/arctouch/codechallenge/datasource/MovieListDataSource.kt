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

abstract class MovieListDataSource(
        private val scope: CoroutineScope
) : PageKeyedDataSource<Long, Movie>(), KoinComponent {

    protected val movieRepository by inject<MovieRepository>()

    private val _initialLoadingLiveData = MutableLiveData<Boolean>(false)
    val initialLoadingLiveData: LiveData<Boolean> get() = _initialLoadingLiveData

    private val _pageRequestLoadingLiveData = MutableLiveData<Boolean>(false)
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

    abstract suspend fun loadInitialFromService(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Long, Movie>)
    abstract suspend fun loadPageFromService(currentPageKey: Long, adjacentPageKey: Long, callback: LoadCallback<Long, Movie>)

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Long, Movie>) {
        scope.launch(Dispatchers.Main + initialLoadCoroutineExceptionHandler) {
            _initialLoadingLiveData.value = true
            withContext(Dispatchers.IO + initialLoadCoroutineExceptionHandler) {
                loadInitialFromService(params, callback)
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
                loadPageFromService(currentPageKey, adjacentPageKey, callback)
            }
            _pageRequestLoadingLiveData.value = false
        }
    }
}