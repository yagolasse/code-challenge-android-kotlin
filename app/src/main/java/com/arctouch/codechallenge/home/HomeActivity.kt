package com.arctouch.codechallenge.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.data.Cache
import kotlinx.android.synthetic.main.home_activity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class HomeActivity : AppCompatActivity() {

    private val api by inject<TmdbApi>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        GlobalScope.launch(Dispatchers.IO) {
            val upcomingMovies = api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, 1, TmdbApi.DEFAULT_REGION)
            val moviesWithGenres = upcomingMovies.results.map { movie ->
                movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
            }
            withContext(Dispatchers.Main) {
                recyclerView.adapter = HomeAdapter(moviesWithGenres)
                progressBar.visibility = View.GONE
            }
        }
    }
}
