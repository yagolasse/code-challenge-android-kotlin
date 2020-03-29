package com.arctouch.codechallenge.viewmodel

import androidx.lifecycle.*
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.repository.MovieRepository
import com.arctouch.codechallenge.util.SingleLiveEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

@ExperimentalCoroutinesApi
class DetailViewModel(val id: Int) : ViewModel(), KoinComponent {

    private val movieRepository by inject<MovieRepository>()

    private val _errorLiveData = MutableLiveData<Unit>()
    val errorLiveData: LiveData<Unit> = _errorLiveData

    val movieLiveData: LiveData<Movie>

    init {
        movieLiveData = movieRepository
                .getMovie(id.toLong())
                .catch { _errorLiveData.postValue(Unit) }
                .flowOn(Dispatchers.IO)
                .asLiveData(Dispatchers.Main)
    }
}