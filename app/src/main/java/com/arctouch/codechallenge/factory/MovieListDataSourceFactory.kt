package com.arctouch.codechallenge.factory

import androidx.paging.DataSource
import com.arctouch.codechallenge.datasource.MovieListDataSource
import com.arctouch.codechallenge.model.Movie
import kotlinx.coroutines.CoroutineScope

class MovieListDataSourceFactory(
        private val scope: CoroutineScope,
        private val query: String?
) : DataSource.Factory<Long, Movie>() {

    override fun create(): DataSource<Long, Movie> = MovieListDataSource(scope, query)

}