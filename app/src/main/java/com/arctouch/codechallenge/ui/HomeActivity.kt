package com.arctouch.codechallenge.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.ui.DetailActivity.Companion.MOVIE_ID_KEY
import com.arctouch.codechallenge.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.home_activity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

        with(movieListViewModel) {
            movieListLiveData.observe(this@HomeActivity, Observer { dataChunk ->
                showErrorView(false)
                adapter.submitList(dataChunk)
            })

            initialLoadingLiveData.observe(this@HomeActivity, Observer { isLoading ->
                progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            })

            pageRequestLoadingLiveData.observe(this@HomeActivity, Observer { isLoading ->
                progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            })

            initialLoadErrorEvent.observe(this@HomeActivity, Observer {
                showErrorView(true)
            })

            pageLoadErrorEvent.observe(this@HomeActivity, Observer {
                Toast.makeText(this@HomeActivity, R.string.we_had_some_trouble_retrieving_more_movies, Toast.LENGTH_SHORT).show()
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)

        val searchMenuItem = menu?.findItem(R.id.barSearch)?.apply {
            setOnActionExpandListener(this@HomeActivity)
        } ?: return false

        searchView = (searchMenuItem.actionView as? SearchView)?.apply {
            if (movieListViewModel.currentQuery != null) {
                searchMenuItem.expandActionView()
                setQuery(movieListViewModel.currentQuery, false)
            }
            setOnQueryTextListener(this@HomeActivity)
        } ?: return false

        return true
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        movieListViewModel.queryEvent.call()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        with(movieListViewModel) {
            currentQuery = query
            queryEvent.call()
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        val searchText = newText?.trim()
        if (searchText?.isEmpty() == true || searchText == movieListViewModel.currentQuery)
            return false

        movieListViewModel.currentQuery = searchText

        launch {
            delay(300)
            if (searchText != movieListViewModel.currentQuery)
                return@launch

            movieListViewModel.queryEvent.call()
        }

        return true
    }

    override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
        return true
    }

    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
        searchView.setQuery(null, false)
        with(movieListViewModel) {
            currentQuery = null
            queryEvent.call()
        }
        return true
    }

    private fun showErrorView(shouldShow: Boolean) {
        if (shouldShow) {
            stateTextView.setText(R.string.an_error_has_occurred_try_again)
            stateImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_error_outline))
            stateGroup.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            stateGroup.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    private fun showEmptyListView(shouldShow: Boolean) {
        if (shouldShow) {
            stateTextView.setText(R.string.list_is_empty)
            stateImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_list_alt))
            stateGroup.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            stateGroup.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

}