package com.arctouch.codechallenge.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.arctouch.codechallenge.model.Movie
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

class HomeViewModel : ViewModel(), KoinComponent {

    val movieListLiveData: LiveData<PagedList<Movie>> by inject {
        parametersOf(viewModelScope)
    }

}