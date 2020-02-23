package com.arctouch.codechallenge.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.ui.DetailActivity.Companion.MOVIE_ID_KEY
import com.arctouch.codechallenge.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.home_activity.*
import org.koin.android.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity() {

    private val movieListViewModel by viewModel<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        val adapter = HomePagedAdapter { movieId ->
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra(MOVIE_ID_KEY, movieId)
            }
            startActivity(intent)
        }

        recyclerView.adapter = adapter

        movieListViewModel.movieListLiveData.observe(this, Observer { dataChunk ->
            adapter.submitList(dataChunk)
        })

        progressBar.visibility = View.GONE
    }
}
