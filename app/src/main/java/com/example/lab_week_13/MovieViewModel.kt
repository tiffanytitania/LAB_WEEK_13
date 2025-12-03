package com.example.lab_week_13

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab_week_13.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.Calendar

class MovieViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _popularMovies = MutableStateFlow(emptyList<Movie>())
    val popularMovies: StateFlow<List<Movie>> = _popularMovies

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    init {
        loadMovies()
    }

    private fun loadMovies() {
        viewModelScope.launch {
            movieRepository.fetchMovies()
                .catch { e ->
                    _error.value = e.message ?: "Unknown error"
                }
                .collect { movies ->

                    //filter
                    val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()

                    val filteredMovies = movies
                        .filter { it.releaseDate?.startsWith(currentYear) == true }
                        .sortedByDescending { it.popularity }

                    _popularMovies.value = filteredMovies
                }
        }
    }
}
