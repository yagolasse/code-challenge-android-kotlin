package com.arctouch.codechallenge.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.dao.MovieDao
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.repository.GenreRepository
import kotlinx.android.synthetic.main.home_activity.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject

class HomeActivity : AppCompatActivity() {

    private val movieDao by inject<MovieDao>()
    private val genreRepository by inject<GenreRepository>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        GlobalScope.launch(Dispatchers.IO) {
            val genreResult = async { genreRepository.getGenreList() }
            val upcomingMovies = async {
                movieDao.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, 1, TmdbApi.DEFAULT_REGION)
            }
            Cache.cacheGenres(genreResult.await())
            val moviesWithGenres = upcomingMovies.await().results.map { movie ->
                movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
            }
            withContext(Dispatchers.Main) {
                recyclerView.adapter = HomeAdapter(moviesWithGenres)
                progressBar.visibility = View.GONE
            }
        }
    }
}
