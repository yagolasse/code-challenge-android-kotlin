package com.arctouch.codechallenge.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.arctouch.codechallenge.factory.MovieListDataSourceFactory
import com.arctouch.codechallenge.model.Movie
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

class HomeViewModel : ViewModel(), KoinComponent {

    private val queryLiveData = MutableLiveData<String>(null)
    val latestQuery get() = queryLiveData.value

    private val movieListDataSourceFactory by inject<MovieListDataSourceFactory> {
        parametersOf(viewModelScope)
    }

    val movieListLiveData: LiveData<PagedList<Movie>> = queryLiveData.switchMap { query ->
        movieListDataSourceFactory.query = query
        get<LiveData<PagedList<Movie>>>()
    }

    private val dataSourceLiveData get() = movieListDataSourceFactory.dataSourceLiveData

    val initialLoadingLiveData: LiveData<Boolean> = dataSourceLiveData.switchMap { it.initialLoadingLiveData }
    val pageRequestLoadingLiveData: LiveData<Boolean> = dataSourceLiveData.switchMap { it.pageRequestLoadingLiveData }
    val initialLoadErrorEvent: LiveData<Unit> = dataSourceLiveData.switchMap { it.initialLoadErrorEvent }
    val pageLoadErrorEvent: LiveData<Unit> = dataSourceLiveData.switchMap { it.pageLoadErrorEvent }

    fun setQuery(query: String?) {
        queryLiveData.value = query
    }

}