package com.arctouch.codechallenge.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                movieListViewModel.queryLiveData.value = s?.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used by now
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not used by now
            }

        })

        movieListViewModel.movieListLiveData.observe(this, Observer { dataChunk ->
            adapter.submitList(dataChunk)
        })

        progressBar.visibility = View.GONE
    }
}
