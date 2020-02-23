package com.arctouch.codechallenge.repository

import com.arctouch.codechallenge.constants.Constants.API_KEY
import com.arctouch.codechallenge.constants.Constants.DEFAULT_LANGUAGE
import com.arctouch.codechallenge.dao.GenreDao
import com.arctouch.codechallenge.model.Genre
import org.koin.core.KoinComponent
import org.koin.core.inject

class GenreRepositoryImpl : KoinComponent, GenreRepository {

    private var genres: List<Genre>? = null
    private val genreDao by inject<GenreDao>()

    override suspend fun getGenreList(): List<Genre> {
        if (genres == null) {
            genres = genreDao.genres(API_KEY, DEFAULT_LANGUAGE).genres
        }
        return genres ?: emptyList()
    }
}