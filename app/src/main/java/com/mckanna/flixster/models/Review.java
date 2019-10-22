package com.mckanna.flixster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Review {

    private String author;
    private String content;
    private String url;

    public Review(JSONObject review) throws JSONException{
        this.author = review.getString("author");
        this.content = review.getString("content");
        this.url = review.getString("url");
    }

    public static List<Review> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Review> reviews = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            reviews.add(new Review(jsonArray.getJSONObject((i))));
        }
        return reviews;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }
}
