package com.mckanna.flixster;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

public class FlixsterHttpClient {
    private static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/";
    private static final String API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed";
    private AsyncHttpClient client;

    public FlixsterHttpClient() {
        this.client = new AsyncHttpClient();
    }

    private String getApiUrl(String relativeUrl) {
        return NOW_PLAYING_URL + relativeUrl;
    }

    public void getNowPlayingMovies(JsonHttpResponseHandler handler) {
        String url = getApiUrl("movie/now_playing");
        RequestParams params = new RequestParams();
        params.put("api_key", API_KEY);
        client.get(url, params, handler);
    }
}
