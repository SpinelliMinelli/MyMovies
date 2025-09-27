package com.example.mymovies

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    interface MovieService {
        @GET("movie/popular")
        fun getPopularMovies(
            @Query("api_key") apiKey: String = "d2755858fab37b37ca01e2c06c2181cf",
            @Query("page") page: Int
        ): Call<GetMoviesResponse>

        @GET("movie/top_rated")
        fun getTopRatedMovies(
            @Query("api_key") apiKey: String = "d2755858fab37b37ca01e2c06c2181cf",
            @Query("page") page: Int
        ): Call<GetMoviesResponse>

        @GET("movie/upcoming")
        fun getUpcomingMovies(
            @Query("api_key")  apiKey:  String  =  "d2755858fab37b37ca01e2c06c2181cf",
            @Query("page") page: Int
        ):  Call<GetMoviesResponse>
    }
}

