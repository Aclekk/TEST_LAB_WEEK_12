package com.example.test_lab_week_12.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.test_lab_week_12.model.Movie
import com.example.test_lab_week_12.network.MovieService

class MovieRepository(private val movieService: MovieService) {

    // Ganti dengan API Key kamu
    private val apiKey = "40053f2e8b50b2c49387de51a7b84fe4"

    // LiveData that contains a list of movies
    private val movieLiveData = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>>
        get() = movieLiveData

    // LiveData that contains an error message
    private val errorLiveData = MutableLiveData<String>()
    val error: LiveData<String>
        get() = errorLiveData

    // fetch movies from the API
    suspend fun fetchMovies() {
        try {
            // get the list of popular movies from the API
            val popularMovies = movieService.getPopularMovies(apiKey)
            movieLiveData.postValue(popularMovies.results)
        } catch (exception: Exception) {
            // if an error occurs, post the error message to the errorLiveData
            errorLiveData.postValue("An error occurred: ${exception.message}")
        }
    }
}