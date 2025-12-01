package com.example.test_lab_week_12.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test_lab_week_12.model.Movie
import com.example.test_lab_week_12.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Calendar

class MovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    init {
        fetchPopularMovies()
    }

    // define the StateFlow in replace of the LiveData
    // a StateFlow is an observable Flow that emits state updates to the collectors
    // MutableStateFlow is a StateFlow that you can change the value
    private val _popularMovies = MutableStateFlow(emptyList<Movie>())
    val popularMovies: StateFlow<List<Movie>> = _popularMovies

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    // fetch movies from the API
    private fun fetchPopularMovies() {
        // launch a coroutine in viewModelScope
        viewModelScope.launch(Dispatchers.IO) {
            movieRepository.fetchMovies()
                .map { movies ->
                    // Get current year
                    val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()

                    // Filter movies by current year and sort by popularity descending
                    movies
                        .filter { movie ->
                            movie.releaseDate?.startsWith(currentYear) == true
                        }
                        .sortedByDescending { it.popularity }
                }
                .catch { exception ->
                    // catch is a terminal operator that catches exceptions from the Flow
                    _error.value = "An exception occurred: ${exception.message}"
                }
                .collect { filteredMovies ->
                    // collect is a terminal operator that collects the values from the Flow
                    // the results are emitted to the StateFlow
                    _popularMovies.value = filteredMovies
                }
        }
    }
}