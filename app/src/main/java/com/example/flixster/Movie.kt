package com.example.flixster

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import org.json.JSONArray

// Kotlin bundles up data, overview, poster etc using data class
@Parcelize
data class Movie(
    val movieId: Int,
    private val posterPath: String,
    val overview: String,
    val title: String,
    val voteAverage : Double
): Parcelable {
    @IgnoredOnParcel
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
                        movieJson.getString("overview"),
                        movieJson.getString("title"),
                        movieJson.getDouble("vote_average")
                    )
                )
            }
            return movies
        }
    }
}