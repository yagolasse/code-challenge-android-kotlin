package com.arctouch.codechallenge.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.util.MovieImageUrlBuilder
import com.arctouch.codechallenge.util.loads
import com.arctouch.codechallenge.util.on
import com.arctouch.codechallenge.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.activity_detail.*
import org.koin.android.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {

    private val detailViewModel by viewModel<DetailViewModel>()
    private val movieImageUrlBuilder = MovieImageUrlBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        val movieId = intent.getIntExtra(MOVIE_ID_KEY, -1)

        if (movieId == -1) return

        detailViewModel.loadMovie(movieId)

        detailViewModel.movieLiveData.observe(this, Observer { movie ->
            titleTextView.text = movie.title
            genresTextView.text = movie.genres?.joinToString(separator = ", ") { it.name }
            releaseDateTextView.text = movie.releaseDate

            val posterUrl = movie.posterPath?.let { movieImageUrlBuilder.buildPosterUrl(it) }

            this loads posterUrl on posterImageView
        })

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val MOVIE_ID_KEY = "movieId"
    }
}