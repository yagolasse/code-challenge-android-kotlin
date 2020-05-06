package com.arctouch.codechallenge.repository

import com.arctouch.codechallenge.model.Genre
import kotlinx.coroutines.flow.Flow

interface GenreRepository {

    suspend fun getGenreList(): Flow<List<Genre>>
}