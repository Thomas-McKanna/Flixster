package com.mckanna.flixster;

import android.content.Context;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

public class FlixsterHttpClient {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String NOW_PLAYING_URL = "movie/now_playing";
    private static final String DETAILS_URL = "movie/";
    private AsyncHttpClient client;

    private static final String TAG = FlixsterHttpClient.class.getSimpleName();

    // The Movie Database API key
    private String TMDB_API_KEY;
    // Youtube Data API v3 key
    private String YOUTUBE_API_KEY;

    public FlixsterHttpClient(Context context) {
        this.client = new AsyncHttpClient();

        this.TMDB_API_KEY = context.getString(R.string.themoviedbApiKey);
        this.YOUTUBE_API_KEY = context.getString(R.string.youtubeApiKey);
    }

    private String getApiUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    public void getNowPlayingMovies(JsonHttpResponseHandler handler) {
        String url = getApiUrl(NOW_PLAYING_URL);
        RequestParams params = new RequestParams();
        params.put("api_key", this.TMDB_API_KEY);
        client.get(url, params, handler);
    }

    public void getMovieDetails(Integer id, JsonHttpResponseHandler handler) {
        String url = getApiUrl(DETAILS_URL + Integer.toString(id));
        RequestParams params = new RequestParams();
        params.put("api_key", this.TMDB_API_KEY);
        client.get(url, params, handler);
    }

    public void getMovieReviews(Integer id, JsonHttpResponseHandler handler) {
        String url = getApiUrl(DETAILS_URL + Integer.toString(id) + "/reviews");
        RequestParams params = new RequestParams();
        params.put("api_key", this.TMDB_API_KEY);
        client.get(url, params, handler);
    }

    public void getMovieVideos(Integer id, JsonHttpResponseHandler handler) {
        String url = getApiUrl(DETAILS_URL + Integer.toString(id) + "/videos");
        Log.d(TAG, url);
        RequestParams params = new RequestParams();
        params.put("api_key", this.TMDB_API_KEY);
        client.get(url, params, handler);
    }
}
