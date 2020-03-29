package com.arctouch.codechallenge.dependencyinjection

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.arctouch.codechallenge.constants.Constants.BASE_URL
import com.arctouch.codechallenge.dao.GenreDao
import com.arctouch.codechallenge.dao.MovieDao
import com.arctouch.codechallenge.factory.MovieListDataSourceFactory
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.repository.GenreRepository
import com.arctouch.codechallenge.repository.GenreRepositoryImpl
import com.arctouch.codechallenge.repository.MovieRepository
import com.arctouch.codechallenge.repository.MovieRepositoryImpl
import com.arctouch.codechallenge.util.MovieImageUrlBuilder
import com.arctouch.codechallenge.viewmodel.DetailViewModel
import com.arctouch.codechallenge.viewmodel.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val networkModule = module {
    factory { OkHttpClient.Builder().build() }
    factory<Converter.Factory> { MoshiConverterFactory.create() }
    single {
        Retrofit.Builder().baseUrl(BASE_URL).client(get()).addConverterFactory(get()).build()
    }
}

val daoModule = module {
    single { get<Retrofit>().create(GenreDao::class.java) }
    single { get<Retrofit>().create(MovieDao::class.java) }
}

val repositoryModule = module {
    single<GenreRepository> { GenreRepositoryImpl() }
    single<MovieRepository> { MovieRepositoryImpl() }
}

val dataSourceModule = module {
    factory { PagedList.Config.Builder().setEnablePlaceholders(false).setPageSize(20).build() }
    single { (scope: CoroutineScope) -> MovieListDataSourceFactory(scope) }
    factory<LiveData<PagedList<Movie>>> {
        val dataSourceFactory = get<MovieListDataSourceFactory>()
        val config = get<PagedList.Config>()
        LivePagedListBuilder<Long, Movie>(dataSourceFactory, config).build()
    }
}

@ExperimentalCoroutinesApi
val viewModelModule = module {
    viewModel { HomeViewModel() }
    viewModel { (id: Int) -> DetailViewModel(id) }
}

val utilsModule = module {
    single { MovieImageUrlBuilder() }
}