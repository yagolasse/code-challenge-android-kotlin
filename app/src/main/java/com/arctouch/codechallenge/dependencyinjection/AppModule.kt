package com.arctouch.codechallenge.dependencyinjection

import com.arctouch.codechallenge.api.TmdbApi
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {
    single<TmdbApi> {
        Retrofit.Builder()
                .baseUrl(TmdbApi.URL)
                .client(OkHttpClient.Builder().build())
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(TmdbApi::class.java)
    }
}