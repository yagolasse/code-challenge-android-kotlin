package com.arctouch.codechallenge.repository

import com.arctouch.codechallenge.model.Genre

interface GenreRepository {

    suspend fun getGenreList(): List<Genre>
}