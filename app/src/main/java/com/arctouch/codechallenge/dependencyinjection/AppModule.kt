package com.arctouch.codechallenge.dependencyinjection

import com.arctouch.codechallenge.repository.GenreRepository
import com.arctouch.codechallenge.repository.GenreRepositoryImpl
import com.arctouch.codechallenge.dao.GenreDao
import com.arctouch.codechallenge.dao.MovieDao
import com.arctouch.codechallenge.api.TmdbApi
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val networkModule = module {
    single {
        Retrofit.Builder()
                .baseUrl(TmdbApi.URL)
                .client(OkHttpClient.Builder().build())
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
    }
}

val daoModule = module {
    single { get<Retrofit>().create(GenreDao::class.java) }
    single { get<Retrofit>().create(MovieDao::class.java) }
}

val repositoryModule = module {
    single<GenreRepository> { GenreRepositoryImpl() }
}