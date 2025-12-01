package com.example.test_lab_week_12

import android.app.Application
import com.example.test_lab_week_12.network.MovieService
import com.example.test_lab_week_12.repository.MovieRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MovieApplication : Application() {

    lateinit var movieRepository: MovieRepository

    override fun onCreate() {
        super.onCreate()

        // create a Moshi instance
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        // create a Retrofit instance
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        // create a MovieService instance
        val movieService = retrofit.create(MovieService::class.java)

        // create a MovieRepository instance
        movieRepository = MovieRepository(movieService)
    }
}