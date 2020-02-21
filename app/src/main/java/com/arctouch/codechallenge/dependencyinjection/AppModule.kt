package com.arctouch.codechallenge.dependencyinjection

import com.arctouch.codechallenge.HomeViewModel
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.dao.GenreDao
import com.arctouch.codechallenge.dao.MovieDao
import com.arctouch.codechallenge.repository.GenreRepository
import com.arctouch.codechallenge.repository.GenreRepositoryImpl
import com.arctouch.codechallenge.repository.MovieRepository
import com.arctouch.codechallenge.repository.MovieRepositoryImpl
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
        Retrofit.Builder().baseUrl(TmdbApi.URL).client(get()).addConverterFactory(get()).build()
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

val viewModelModule = module {
    viewModel { HomeViewModel() }
}