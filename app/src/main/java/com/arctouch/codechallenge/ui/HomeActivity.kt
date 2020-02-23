package com.arctouch.codechallenge.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.ui.DetailActivity.Companion.MOVIE_ID_KEY
import com.arctouch.codechallenge.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.home_activity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.coroutines.CoroutineContext


class HomeActivity : AppCompatActivity(), CoroutineScope, SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    private val movieListViewModel by viewModel<HomeViewModel>()
    override val coroutineContext: CoroutineContext get() = Dispatchers.Main

    private lateinit var searchView: SearchView

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)

        val searchMenuItem = menu?.findItem(R.id.barSearch)?.apply {
            setOnActionExpandListener(this@HomeActivity)
        } ?: return false

        searchView = (searchMenuItem.actionView as? SearchView)?.apply {
            if (movieListViewModel.latestQuery != null) {
                searchMenuItem.expandActionView()
                setQuery(movieListViewModel.latestQuery, false)
            }
            setOnQueryTextListener(this@HomeActivity)
        } ?: return false

        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        movieListViewModel.setQuery(query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        movieListViewModel.setQuery(newText)
        return true
    }

    override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
        return true
    }

    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
        searchView.setQuery(null, false)
        movieListViewModel.setQuery(null)
        return true
    }

}
