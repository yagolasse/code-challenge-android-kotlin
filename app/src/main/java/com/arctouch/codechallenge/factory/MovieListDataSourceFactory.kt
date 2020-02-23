package com.arctouch.codechallenge.factory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.arctouch.codechallenge.datasource.MovieListDataSource
import com.arctouch.codechallenge.model.Movie
import kotlinx.coroutines.CoroutineScope

class MovieListDataSourceFactory(
        private val scope: CoroutineScope
) : DataSource.Factory<Long, Movie>() {

    var query: String? = null

    private val _dataSourceLiveData = MutableLiveData<MovieListDataSource>()
    val dataSourceLiveData: LiveData<MovieListDataSource> get() = _dataSourceLiveData

    override fun create(): DataSource<Long, Movie> {
        return MovieListDataSource(scope, query).also {
            _dataSourceLiveData.postValue(it)
        }
    }

}