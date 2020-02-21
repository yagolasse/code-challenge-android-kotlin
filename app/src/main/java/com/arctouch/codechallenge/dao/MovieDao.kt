package com.arctouch.codechallenge.dao

import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.model.UpcomingMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDao {

    @GET("movie/upcoming")
    suspend fun upcomingMovies(
            @Query("api_key") apiKey: String,
            @Query("language") language: String,
            @Query("page") page: Long,
            @Query("region") region: String
    ): UpcomingMoviesResponse

    @GET("movie/{id}")
    suspend fun movie(
            @Path("id") id: Long,
            @Query("api_key") apiKey: String,
            @Query("language") language: String
    ): Movie

}