package com.arctouch.codechallenge

import androidx.paging.DataSource
import com.arctouch.codechallenge.datasource.MovieListDataSource
import com.arctouch.codechallenge.model.Movie
import kotlinx.coroutines.CoroutineScope

class MovieListDataSourceFactory(
        private val scope: CoroutineScope
) : DataSource.Factory<Long, Movie>() {

    override fun create(): DataSource<Long, Movie> = MovieListDataSource(scope)

}