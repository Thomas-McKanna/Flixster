package com.mckanna.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mckanna.flixster.models.Movie;

import org.parceler.Parcels;

public class MovieDetailsActivity extends AppCompatActivity {

    private Movie movie;

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

        // Unwrap the movie passed in via intent
        Parcelable parcel = getIntent().getParcelableExtra(Movie.class.getSimpleName());
        movie = (Movie) Parcels.unwrap(parcel);
        Log.d(getClass().getSimpleName(), String.format("Showing details for '%s'", movie.getTitle()));

        // Set title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setNumStars(10);
        rbVoteAverage.setRating(voteAverage);
    }
}
