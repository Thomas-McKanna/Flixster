package com.mckanna.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.mckanna.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import okhttp3.Headers;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailsActivity.class.getSimpleName();
    private static final String WEBSITE = "YouTube";
    private static final String VID_TYPE = "Trailer";

    // Intent key value
    public static final String VID_ID = "video_id";

    private Movie movie;
    private String youtubeId;

    private ImageView ivBackdrop;
    private TextView tvTitle;
    private TextView tvOverview;
    private RatingBar rbVoteAverage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // Resolve view objects
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        ivBackdrop = (ImageView) findViewById(R.id.ivBackdrop);

        // Unwrap the movie passed in via intent
        Parcelable parcel = getIntent().getParcelableExtra(Movie.class.getSimpleName());
        movie = (Movie) Parcels.unwrap(parcel);
        Log.d(TAG, String.format("Showing details for '%s'", movie.getTitle()));

        // Set title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        float voteAverage = movie.getVoteAverage().floatValue() / 2.0f;
        rbVoteAverage.setRating(voteAverage);

        // Set the backdrop
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.popcorn);

        Glide.with(this)
                // Shows a loading gif while the image loads
                .setDefaultRequestOptions(requestOptions)
                .load(movie.getBackdropPath())
                .into(ivBackdrop);

        // Set OnClickListener for ImageView
        ivBackdrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchTrailer();
            }
        });

        // Set the YouTube ID to initially be null
        youtubeId = null;

        // Try to fetch the YouTube ID using TMDB API
        FlixsterHttpClient client = new FlixsterHttpClient(this);
        client.getMovieVideos(movie.id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");

                    String site, type;
                    // Try to find a video which is on YouTube and is a Trailer
                    for (int i = results.length() - 1; i >= 0; i--) {
                        site = results.getJSONObject(i).getString("site");
                        type = results.getJSONObject(i).getString("type");

                        if (site.equals(WEBSITE) && type.equals(VID_TYPE)) {
                            youtubeId = results.getJSONObject(i).getString("key");

                            if (movie.getVoteAverage() >= Movie.VOTE_THRESHOLD) {
                                launchTrailer();
                            }

                            break;
                        }
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "Failed to retrieve movie videos");
            }
        });
    }

    private void launchTrailer() {
        if (youtubeId != null) {
            Intent intent = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
            intent.putExtra(MovieDetailsActivity.VID_ID, youtubeId);

            MovieDetailsActivity.this.startActivity(intent);
        }
    }
}
