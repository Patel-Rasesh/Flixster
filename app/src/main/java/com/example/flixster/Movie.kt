package com.example.flixster

import org.json.JSONArray

// Kotlin bundles up data, overview, poster etc using data class
data class Movie(
    val movieId: Int,
    private val posterPath: String,
    val overview: String,
    val title: String
){
    val posterImageURL = "https://image.tmdb.org/t/p/w342/$posterPath"
    // companion object allows me to call method without instantiating an object
    companion object{
        fun fromJsonArray(movieJsonArray: JSONArray): List<Movie>{
            val movies = mutableListOf<Movie>()
            for (i in 0 until movieJsonArray.length()) {
                val movieJson = movieJsonArray.getJSONObject(i)
                movies.add(
                    Movie(
                        movieJson.getInt("id"),
                        movieJson.getString("poster_path"),
                        movieJson.getString("title"),
                        movieJson.getString("overview")
                    )
                )
            }
            return movies
        }
    }
}