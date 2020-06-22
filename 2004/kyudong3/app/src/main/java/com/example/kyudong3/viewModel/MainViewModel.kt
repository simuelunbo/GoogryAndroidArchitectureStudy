package com.example.kyudong3.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kyudong3.R
import com.example.kyudong3.provider.ResourceProvider
import com.kyudong.data.model.Movie
import com.kyudong.data.repository.MovieRepository

class MainViewModel(
    private val movieRepository: MovieRepository,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>>
        get() = _movies

    private val _errorSearchResult = MutableLiveData<String>()
    val errorSearchResult: LiveData<String>
        get() = _errorSearchResult

    val searchQuery = MutableLiveData<String>()

    fun searchMovie() {
        val query = searchQuery.value
        if (query.isNullOrBlank()) {
            _errorSearchResult.value =
                resourceProvider.getString(R.string.toast_invalid_search_query)
        } else {
            fetchMovieList(query)
        }
    }

    private fun fetchMovieList(searchQuery: String) {
        movieRepository.getMovieListRemote(searchQuery,
            success = { movieList: List<Movie> ->
                if (movieList.isEmpty()) {
                    _errorSearchResult.value =
                        resourceProvider.getString(R.string.toast_empty_search_result)
                } else {
                    _movies.value = movieList
                }
            },
            failure = {
                _errorSearchResult.value =
                    resourceProvider.getString(R.string.toast_show_network_error)
            })
    }
}