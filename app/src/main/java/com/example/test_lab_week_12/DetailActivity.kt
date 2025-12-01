package com.example.test_lab_week_12

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Get data from Intent
        val title = intent.getStringExtra("MOVIE_TITLE") ?: "Unknown Title"
        val overview = intent.getStringExtra("MOVIE_OVERVIEW") ?: "No overview available"
        val posterPath = intent.getStringExtra("MOVIE_POSTER")
        val releaseDate = intent.getStringExtra("MOVIE_RELEASE_DATE") ?: "Unknown"
        val rating = intent.getDoubleExtra("MOVIE_RATING", 0.0)

        // Find views
        val titleTextView = findViewById<TextView>(R.id.detail_title)
        val overviewTextView = findViewById<TextView>(R.id.detail_overview)
        val releaseDateTextView = findViewById<TextView>(R.id.detail_release_date)
        val ratingTextView = findViewById<TextView>(R.id.detail_rating)
        val posterImageView = findViewById<ImageView>(R.id.detail_poster)

        // Set data
        titleTextView.text = title
        overviewTextView.text = overview
        releaseDateTextView.text = "Release: $releaseDate"
        ratingTextView.text = "Rating: $rating"

        // Load poster image
        posterPath?.let {
            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500$it")
                .into(posterImageView)
        }
    }
}