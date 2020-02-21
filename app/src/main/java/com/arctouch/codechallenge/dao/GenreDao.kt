package com.arctouch.codechallenge.dao

import com.arctouch.codechallenge.model.GenreResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GenreDao {

    @GET("genre/movie/list")
    suspend fun genres(
            @Query("api_key") apiKey: String,
            @Query("language") language: String
    ): GenreResponse

}