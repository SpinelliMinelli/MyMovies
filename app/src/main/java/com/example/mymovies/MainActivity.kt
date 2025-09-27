package com.example.mymovies

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var popularMovies: RecyclerView
    private lateinit var popularMoviesAdapter: MoviesAdapter

    private lateinit var topRatedMovies: RecyclerView
    private lateinit var topRatedMoviesAdapter: MoviesAdapter

    private lateinit var upcomingMovies: RecyclerView
    private lateinit var upcomingMoviesAdapter: MoviesAdapter
    private lateinit var upcomingMoviesLayoutMgr: LinearLayoutManager
    private var upcomingMoviesPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Popular Movies RecyclerView
        popularMovies = findViewById(R.id.popular_movies)
        popularMovies.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        popularMoviesAdapter = MoviesAdapter(mutableListOf()) { movie ->
            showMovieDetails(movie)
        }
        popularMovies.adapter = popularMoviesAdapter

        // Initialize Top Rated Movies RecyclerView
        topRatedMovies = findViewById(R.id.top_rated_movies)
        topRatedMovies.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        topRatedMoviesAdapter = MoviesAdapter(mutableListOf()) { movie ->
            showMovieDetails(movie)
        }
        topRatedMovies.adapter = topRatedMoviesAdapter

        // Initialize Upcoming Movies RecyclerView
        upcomingMovies = findViewById(R.id.upcoming_movies)
        upcomingMoviesLayoutMgr = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        upcomingMovies.layoutManager = upcomingMoviesLayoutMgr
        upcomingMoviesAdapter = MoviesAdapter(mutableListOf()) { movie ->
            showMovieDetails(movie)
        }
        upcomingMovies.adapter = upcomingMoviesAdapter

        // Fetch data for each category
        getPopularMovies()
        getTopRatedMovies()
        getUpcomingMovies()

        // Attach scroll listener for pagination on upcoming movies
        attachUpcomingMoviesOnScrollListener()
    }

    private fun getPopularMovies() {
        MoviesRepository.getPopularMovies(
            page = 1,
            onSuccess = { movies ->
                Log.d("MainActivity", "Popular movies: $movies")
                popularMoviesAdapter.updateMovies(movies)
            },
            onError = {
                Toast.makeText(this, getString(R.string.error_fetch_movies), Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun getTopRatedMovies() {
        MoviesRepository.getTopRatedMovies(
            page = 1,
            onSuccess = { movies ->
                Log.d("MainActivity", "Top Rated movies: $movies")
                topRatedMoviesAdapter.updateMovies(movies)
            },
            onError = {
                Toast.makeText(this, "Error fetching top rated movies", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun getUpcomingMovies() {
        MoviesRepository.getUpcomingMovies(
            page = upcomingMoviesPage,
            onSuccess = { movies ->
                Log.d("MainActivity", "Upcoming movies: $movies")
                // Append new movies to the existing list
                upcomingMoviesAdapter.appendMovies(movies)
            },
            onError = {
                Toast.makeText(this, "Error fetching upcoming movies", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun attachUpcomingMoviesOnScrollListener() {
        upcomingMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = upcomingMoviesLayoutMgr.itemCount
                val visibleItemCount = upcomingMoviesLayoutMgr.childCount
                val firstVisibleItem = upcomingMoviesLayoutMgr.findFirstVisibleItemPosition()

                // When scrolled past half of the total items, load the next page
                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    upcomingMovies.removeOnScrollListener(this)
                    upcomingMoviesPage++
                    getUpcomingMovies()
                }
            }
        })
    }

    private fun showMovieDetails(movie: Movie) {
        val intent = Intent(this, MovieDetailsActivity::class.java).apply {
            putExtra(MOVIE_BACKDROP, movie.backdropPath)
            putExtra(MOVIE_POSTER, movie.posterPath)
            putExtra(MOVIE_TITLE, movie.title)
            putExtra(MOVIE_RATING, movie.rating)
            putExtra(MOVIE_RELEASE_DATE, movie.releaseDate)
            putExtra(MOVIE_OVERVIEW, movie.overview)
        }
        startActivity(intent)
    }
}
