package com.example.test_lab_week_12

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test_lab_week_12.adapter.MovieAdapter
import com.example.test_lab_week_12.model.Movie
import com.example.test_lab_week_12.viewmodel.MovieViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var movieAdapter: MovieAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup RecyclerView
        recyclerView = findViewById(R.id.movie_list)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize adapter with click listener
        movieAdapter = MovieAdapter { movie ->
            navigateToDetail(movie)
        }
        recyclerView.adapter = movieAdapter

        // Get repository from Application
        val movieRepository = (application as MovieApplication).movieRepository

        // Create ViewModel
        val movieViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MovieViewModel(movieRepository) as T
            }
        })[MovieViewModel::class.java]

        // Collect flows
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Collect movies
                launch {
                    movieViewModel.popularMovies.collect { movies ->
                        movieAdapter.addMovies(movies)
                    }
                }

                // Collect errors
                launch {
                    movieViewModel.error.collect { error ->
                        if (error.isNotEmpty()) {
                            Snackbar.make(recyclerView, error, Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    private fun navigateToDetail(movie: Movie) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra("MOVIE_TITLE", movie.title)
            putExtra("MOVIE_OVERVIEW", movie.overview)
            putExtra("MOVIE_POSTER", movie.posterPath)
            putExtra("MOVIE_RELEASE_DATE", movie.releaseDate)
            putExtra("MOVIE_RATING", movie.voteAverage)
        }
        startActivity(intent)
    }
}