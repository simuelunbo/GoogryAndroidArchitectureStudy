package com.eunice.eunicehong.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.eunice.eunicehong.R
import com.eunice.eunicehong.data.model.Movie
import com.eunice.eunicehong.databinding.ItemMovieBinding

class MovieAdapter :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private val movieList = mutableListOf<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemMovieBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_movie,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = movieList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            bind(movieList[position])
        }
    }

    private fun addAllMovies(movies: Collection<Movie>?) {
        movieList.addAll(movies ?: listOf())
        notifyDataSetChanged()
    }

    fun setMovieList(movies: Collection<Movie>?) {
        movieList.clear()
        addAllMovies(movies)
    }

    class ViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Movie) {
            binding.movie = item
            binding.viewHolder = this@ViewHolder

            binding.executePendingBindings()
        }

    }
}