package com.mckanna.flixster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mckanna.flixster.GlideApp;
import com.mckanna.flixster.R;
import com.mckanna.flixster.models.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private Context context;
    private List<Movie> movies;

    public final int SMALL = 0, LARGE = 1;

    public final double VOTE_THRESHOLD = 7.0;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");

        ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case SMALL:
                View v1 = inflater.inflate(R.layout.item_movie_small, parent, false);
                viewHolder = new ViewHolder(v1, false);
                break;
            case LARGE:
                View v2 = inflater.inflate(R.layout.item_movie_large, parent, false);
                viewHolder = new ViewHolder(v2, true);
                break;
            default:
                View v3 = inflater.inflate(R.layout.item_movie_small, parent, false);
                viewHolder = new ViewHolder(v3, false);
        }

        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder " + position);
        // Get the movie at the passed-in position
        Movie movie = movies.get(position);
        // Bind the movie data into the VH
        holder.bind(movie);
    }

    @Override
    public int getItemViewType(int position) {
        if (movies.get(position).getVoteAverage() < VOTE_THRESHOLD) {
            return SMALL;
        } else {
            return LARGE;
        }
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;
        boolean useBackdrop;

        public ViewHolder(@NonNull View itemView, boolean useWide) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            useBackdrop= useWide;
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            String imageUrl;

            if (useBackdrop || context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageUrl = movie.getBackdropPath();
            } else {
                imageUrl = movie.getPosterPath();
            }

            GlideApp.with(context)
                    .load(imageUrl)
                    // Shows a loading gif while the image loads
                    .placeholder(R.drawable.popcorn)
                    .fitCenter()
                    .into(ivPoster);
        }
    }


}
