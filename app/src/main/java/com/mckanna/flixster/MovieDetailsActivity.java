package com.mckanna.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import com.mckanna.flixster.models.Movie;

import org.parceler.Parcels;

public class MovieDetailsActivity extends AppCompatActivity {

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // Unwrap the movie passed in via intent
        Parcelable parcel = getIntent().getParcelableExtra(Movie.class.getSimpleName());
        movie = (Movie) Parcels.unwrap(parcel);
        Log.d(getClass().getSimpleName(), String.format("Showing details for '%s'", movie.getTitle()));

    }
}
