package com.arctouch.codechallenge.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.arctouch.codechallenge.HomeViewModel
import com.arctouch.codechallenge.R
import kotlinx.android.synthetic.main.home_activity.*
import org.koin.android.ext.android.inject

class HomeActivity : AppCompatActivity() {

    private val movieListViewModel by inject<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        movieListViewModel.getMovieList().observe(this, Observer { movies ->
            recyclerView.adapter = HomeAdapter(movies)
            progressBar.visibility = View.GONE
        })
    }
}
