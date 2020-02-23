package com.arctouch.codechallenge.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class DetailViewModel : ViewModel(), KoinComponent {

    private val movieRepository by inject<MovieRepository>()

    private val _movieLiveData = MutableLiveData<Movie>()
    val movieLiveData: LiveData<Movie> get() = _movieLiveData

    fun loadMovie(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _movieLiveData.postValue(movieRepository.getMovie(id.toLong()))
        }
    }

}