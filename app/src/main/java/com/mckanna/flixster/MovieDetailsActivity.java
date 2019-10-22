package com.mckanna.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.mckanna.flixster.adapters.DetailsAdapter;
import com.mckanna.flixster.adapters.MovieAdapter;
import com.mckanna.flixster.models.Movie;
import com.mckanna.flixster.models.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import okhttp3.Headers;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    // Intent key value
    public static final String VID_ID = "video_id";

    private Movie movie;
    private List<Review> reviews;

    private RecyclerView rvDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        reviews = new ArrayList<>();

        // Resolve view objects
        rvDetails = (RecyclerView) findViewById(R.id.rvDetails);

        // Unwrap the movie passed in via intent
        Parcelable parcel = getIntent().getParcelableExtra(Movie.class.getSimpleName());
        movie = (Movie) Parcels.unwrap(parcel);
        Log.d(TAG, String.format("Showing details for '%s'", movie.getTitle()));

        // Create an adapter
        final DetailsAdapter detailsAdapter = new DetailsAdapter(this, movie, reviews);

        // Make animation adapter and set fade in to take 1 second
//        AlphaInAnimationAdapter movieAnimAdapter = new AlphaInAnimationAdapter(movieAdapter);
//        movieAnimAdapter.setDuration(1000);

        // Set the adapter on the recycler view
        rvDetails.setAdapter(detailsAdapter);

        // Set a Layout Manager on the recycler view
        rvDetails.setLayoutManager(new LinearLayoutManager(this));

        detailsAdapter.notifyItemChanged(0);

        // Get reviews for this movie
        FlixsterHttpClient client = new FlixsterHttpClient(this);

        client.getMovieReviews(movie.id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results:" + results.toString());
                    reviews.addAll(Review.fromJsonArray(results));
                    detailsAdapter.notifyItemRangeChanged(1, reviews.size());
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }
}
