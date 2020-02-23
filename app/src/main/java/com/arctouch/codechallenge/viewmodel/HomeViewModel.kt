package com.arctouch.codechallenge.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.arctouch.codechallenge.model.Movie
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.parameter.parametersOf

class HomeViewModel : ViewModel(), KoinComponent {

    private val queryLiveData = MutableLiveData<String>(null)
    val latestQuery get() = queryLiveData.value

    val movieListLiveData: LiveData<PagedList<Movie>> = queryLiveData.switchMap { query ->
        get<LiveData<PagedList<Movie>>> { parametersOf(viewModelScope, query) }
    }

    fun setQuery(query: String?) {
        queryLiveData.value = query
    }

}