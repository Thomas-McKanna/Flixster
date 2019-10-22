package com.mckanna.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.mckanna.flixster.FlixsterHttpClient;
import com.mckanna.flixster.MovieDetailsActivity;
import com.mckanna.flixster.MovieTrailerActivity;
import com.mckanna.flixster.R;
import com.mckanna.flixster.models.Movie;
import com.mckanna.flixster.models.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class DetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = DetailsAdapter.class.getSimpleName();
    private static final String WEBSITE = "YouTube";
    private static final String VID_TYPE = "Trailer";

    private Context context;
    private Movie movie;
    private List<Review> reviews;

    public final int DETAILS = 0, REVIEW = 1;

    public DetailsAdapter(Context context, Movie movie, List<Review> reviews) {
        this.context = context;
        this.movie = movie;
        this.reviews = reviews;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case DETAILS:
                View v1 = inflater.inflate(R.layout.item_movie_details, parent, false);
                viewHolder = new DetailsViewHolder(v1, false);
                break;
            default:
                // Must be a review
                View v2 = inflater.inflate(R.layout.item_review, parent, false);
                viewHolder = new ReviewViewHolder(v2, true);
                break;
        }

        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder " + position);
        switch (holder.getItemViewType()) {
            case DETAILS:
                DetailsViewHolder vh1 = (DetailsViewHolder) holder;
                configureDetailsViewHolder(vh1, position);
                break;
            default:
                ReviewViewHolder vh = (ReviewViewHolder) holder;
                configureReviewViewHolder(vh, position);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return DETAILS;
        } else {
            return REVIEW;
        }
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        // Plus 1 for the detail section
        return reviews.size() + 1;
    }

    private void configureDetailsViewHolder(DetailsViewHolder vh, int position) {
        vh.bind();
    }

    private void configureReviewViewHolder(ReviewViewHolder vh, int position) {
        Review review = reviews.get(position - 1);
        Log.d(TAG, review.getAuthor());
        vh.bind(review);
    }

    public class DetailsViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivBackdrop;
        private TextView tvTitle;
        private TextView tvOverview;
        private TextView tvVoteCount;
        private RatingBar rbVoteAverage;

        private String youtubeId;

        public DetailsViewHolder(@NonNull View itemView, Boolean useWide) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivBackdrop = itemView.findViewById(R.id.ivBackdrop);
            tvVoteCount = itemView.findViewById(R.id.tvVotesCount);
            rbVoteAverage = itemView.findViewById(R.id.rbVoteAverage);
        }

        public void bind() {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            tvVoteCount.setText(movie.getVoteCount().toString());
            String imageUrl = movie.getBackdropPath();

            // Set vote count
            tvVoteCount.setText(movie.getVoteCount().toString() + " votes");

            // Set rating
            float voteAverage = movie.getVoteAverage().floatValue() / 2.0f;
            rbVoteAverage.setRating(voteAverage);

            // Set the backdrop
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.popcorn);

            Glide.with(context)
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
            FlixsterHttpClient client = new FlixsterHttpClient(context);
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
                Intent intent = new Intent(context, MovieTrailerActivity.class);
                intent.putExtra(MovieDetailsActivity.VID_ID, youtubeId);

                context.startActivity(intent);
            }
        }
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvContents;

        private String url;

        public ReviewViewHolder(@NonNull View itemView, Boolean useWide) {
            super(itemView);

            tvName = itemView.findViewById(R.id.reviewerName);
            tvContents = itemView.findViewById(R.id.contents);
        }

        public void bind(Review review) {
            tvName.setText(review.getAuthor());
            tvContents.setText(review.getContent());

            url = review.getUrl();
        }

    }
}
