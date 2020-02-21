package com.arctouch.codechallenge.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.repository.MovieRepository
import kotlinx.android.synthetic.main.home_activity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class HomeActivity : AppCompatActivity() {

    private val movieRepository by inject<MovieRepository>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        GlobalScope.launch(Dispatchers.IO) {
            val movies = movieRepository.getMovieList(1)
            withContext(Dispatchers.Main) {
                recyclerView.adapter = HomeAdapter(movies)
                progressBar.visibility = View.GONE
            }
        }
    }
}
