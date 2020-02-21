package com.arctouch.codechallenge

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import org.koin.core.KoinComponent
import org.koin.core.inject

class HomeViewModel : ViewModel(), KoinComponent {

    private val movieRepository by inject<MovieRepository>()

    fun getMovieList(): LiveData<List<Movie>> = liveData(Dispatchers.IO) {
        emit(movieRepository.getMovieList(1))
    }

}