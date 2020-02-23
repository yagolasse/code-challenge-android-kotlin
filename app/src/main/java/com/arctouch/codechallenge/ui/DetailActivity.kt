package com.arctouch.codechallenge.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.util.MovieImageUrlBuilder
import com.arctouch.codechallenge.util.loads
import com.arctouch.codechallenge.util.on
import com.arctouch.codechallenge.util.withRoundCornersOn
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

        with(detailViewModel) {
            errorEvent.observe(this@DetailActivity, Observer {
                stateGroup.visibility = View.VISIBLE
                dataGroup.visibility = View.GONE
                progressBar.visibility = View.GONE
            })

            movieLiveData.observe(this@DetailActivity, Observer { movie ->
                titleTextView.text = movie.title
                genresTextView.text = movie.genres?.joinToString(separator = ", ") { it.name }
                releaseDateTextView.text = movie.releaseDate
                overviewTextView.text = movie.overview

                val posterUrl = movie.posterPath?.let { movieImageUrlBuilder.buildPosterUrl(it) }
                val backdropUrl = movie.backdropPath?.let { movieImageUrlBuilder.buildBackdropUrl(it) }

                this@DetailActivity loads backdropUrl on backdropImageView
                this@DetailActivity loads posterUrl withRoundCornersOn posterImageView

                dataGroup.visibility = View.VISIBLE
                stateGroup.visibility = View.GONE
                progressBar.visibility = View.GONE
            })

            loadMovie(movieId)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val MOVIE_ID_KEY = "movieId"
    }
}