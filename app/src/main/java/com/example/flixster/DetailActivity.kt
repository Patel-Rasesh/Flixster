package com.example.flixster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RatingBar
import android.widget.TextView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import okhttp3.Headers

private const val YOUTUBE_API_KEY = "AIzaSyB71Eyzh57fwJ221AWKbAiN0nepTbbbePk"
private const val TRAILERS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"
class DetailActivity : YouTubeBaseActivity() {

    private lateinit var tvTitle : TextView
    private lateinit var tvOverview : TextView
    private lateinit var ratingbar : RatingBar
    private lateinit var ytPlayerView : YouTubePlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        tvTitle = findViewById(R.id.tvTitle)
        tvOverview = findViewById(R.id.tvOverview)
        ratingbar = findViewById(R.id.rbVoteAverage)
        ytPlayerView = findViewById(R.id.player)

        val movie = intent.getParcelableExtra<Movie>(MOVIE_EXTRA) as Movie
        Log.i("DetailAct", "Movie is $movie")

        tvTitle.text = movie.title
        tvOverview.text = movie.overview
        ratingbar.rating = movie.voteAverage.toFloat()

        val client = AsyncHttpClient()
        client.get(TRAILERS_URL.format(movie.movieId), object: JsonHttpResponseHandler(){
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e("TRAILERS_URL_Failure", "OnFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.i("TRAILERS_URL_Success", "OnSuccess $statusCode")
                val trailerResults = json.jsonObject.getJSONArray("results")
                if (trailerResults.length() == 0){
                    Log.w("LengthError", "No Movie Trailers available")
                    return
                }
                val movieTrailerJson = trailerResults.getJSONObject(0)
                val youtubekey = movieTrailerJson.getString("key")
                // Play video
                initializeYoutube(youtubekey)
            }

        })
    }

    private fun initializeYoutube(youtubekey: String) {
        ytPlayerView.initialize(YOUTUBE_API_KEY, object:YouTubePlayer.OnInitializedListener{
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                p2: Boolean
            ) {
                Log.i("YTLog", "OnInitializationFailure")
                player?.cueVideo(youtubekey);
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Log.i("YTLog", "OnInitializationFailure")
            }

        })
    }
}