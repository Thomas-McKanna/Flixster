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
import com.mckanna.flixster.models.Movie;

import org.parceler.Parcels;

public class MovieDetailsActivity extends AppCompatActivity {

    private Movie movie;

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
        Log.d(getClass().getSimpleName(), String.format("Showing details for '%s'", movie.getTitle()));

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
                Intent intent = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);

                MovieDetailsActivity.this.startActivity(intent);
            }
        });
    }
}
