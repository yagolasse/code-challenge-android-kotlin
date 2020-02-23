package com.arctouch.codechallenge.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.MovieDiffUtilChecker
import com.arctouch.codechallenge.util.MovieImageUrlBuilder
import com.arctouch.codechallenge.util.asThumbnailInto
import com.arctouch.codechallenge.util.loads
import kotlinx.android.synthetic.main.movie_item.view.*

class HomePagedAdapter(
        private val onMovieClickListener: (Int) -> Unit
) : PagedListAdapter<Movie, HomePagedAdapter.ViewHolder>(MovieDiffUtilChecker()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item, onMovieClickListener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val movieImageUrlBuilder = MovieImageUrlBuilder()

        fun bind(movie: Movie, onMovieClickListener: (Int) -> Unit) {

            with(itemView) {
                titleTextView.text = movie.title
                genresTextView.text = movie.genres?.joinToString(separator = ", ") { it.name }
                releaseDateTextView.text = movie.releaseDate
                setOnClickListener {
                    onMovieClickListener(movie.id)
                }
                val posterUrl = movie.posterPath?.let { movieImageUrlBuilder.buildPosterUrl(it) }

                this loads posterUrl asThumbnailInto itemView.posterImageView
            }
        }
    }
}
