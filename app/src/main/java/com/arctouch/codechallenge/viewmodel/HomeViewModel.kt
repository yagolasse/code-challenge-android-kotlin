package com.arctouch.codechallenge.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.arctouch.codechallenge.factory.MovieListDataSourceFactory
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.SingleLiveEvent
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

class HomeViewModel : ViewModel(), KoinComponent {

    var currentQuery: String? = null
    val queryEvent = SingleLiveEvent<Unit>()

    private val dataSourceLiveData get() = movieListDataSourceFactory.dataSourceLiveData

    private val movieListDataSourceFactory by inject<MovieListDataSourceFactory> {
        parametersOf(viewModelScope)
    }

    val movieListLiveData: LiveData<PagedList<Movie>> = queryEvent.switchMap {
        movieListDataSourceFactory.query = currentQuery
        get<LiveData<PagedList<Movie>>>()
    }

    val initialLoadingLiveData: LiveData<Boolean> = dataSourceLiveData.switchMap { it.initialLoadingLiveData }
    val pageRequestLoadingLiveData: LiveData<Boolean> = dataSourceLiveData.switchMap { it.pageRequestLoadingLiveData }
    val initialLoadErrorEvent: LiveData<Unit> = dataSourceLiveData.switchMap { it.initialLoadErrorEvent }
    val pageLoadErrorEvent: LiveData<Unit> = dataSourceLiveData.switchMap { it.pageLoadErrorEvent }

    init {
        queryEvent.postCall()
    }
}