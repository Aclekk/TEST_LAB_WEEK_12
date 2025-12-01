package com.example.test_lab_week_12.repository

import com.example.test_lab_week_12.model.Movie
import com.example.test_lab_week_12.network.MovieService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MovieRepository(private val movieService: MovieService) {

    private val apiKey = "40053f2e8b50b2c49387de51a7b84fe4"

    // fetch movies from the API
    // this function returns a Flow of Movie objects
    // a Flow is a type of coroutine that can emit multiple values
    fun fetchMovies(): Flow<List<Movie>> {
        return flow {
            // emit the list of popular movies from the API
            emit(movieService.getPopularMovies(apiKey).results)
        }.flowOn(Dispatchers.IO)
    }
}