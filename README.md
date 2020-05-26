# Flixter
Flix is an app that allows users to browse movies from the [The Movie Database API](http://docs.themoviedb.apiary.io/#).

## Flixter Part 2

### User Stories

#### REQUIRED (10pts)

- [x] (8pts) Expose details of movie (ratings using RatingBar, popularity, and synopsis) in a separate activity.
- [x] (2pts) Allow video posts to be played in full-screen using the YouTubePlayerView.

#### BONUS

- [x] Trailers for popular movies are played automatically when the movie is selected (1 point).
- [x] When clicking on a popular movie (i.e. a movie voted for more than 5 stars) the video should be played immediately.
- [x] Less popular videos rely on the detailed page should show an image preview that can initiate playing a YouTube video.
- [x] Add a play icon overlay to popular movies to indicate that the movie can be played (1 point).
- [ ] Apply the popular ButterKnife annotation library to reduce view boilerplate. (1 point)
- [ ] Add a rounded corners for the images using the Glide transformations. (1 point)

### App Walkthough GIF

<img src="https://github.com/Thomas-McKanna/Flixster/raw/master/flixter4.gif" width=250><br>

### Notes


1. Translating RelativeViews to ConstraintViews made it easier for me to scale the app to larger screens (since widths can be set with guidelines) and to overlay the play buttons over images.

2. The RatingBar view does not respond well to padding, but margins work fairly well. You can use the stepSize property when defining the XML for a RatingBar to make the view round the rating to the nearest half-star.

3. Shapes can be defined in the drawable resources file and applied to views (such as for rounded the corners of text views). This technique does not work on ImageViews, though.

## Open-source libraries used
- [YouTube Android Player API](https://developers.google.com/youtube/android/player/) - Playing YouTube videos within the app
- [Android Async HTTP](https://github.com/codepath/CPAsyncHttpClient) - Simple asynchronous HTTP requests with JSON parsing
- [Glide](https://github.com/bumptech/glide) - Image 
---

## Flix Part 1

### User Stories

#### REQUIRED (10pts)
- [x] (10pts) User can view a list of movies (title, poster image, and overview) currently playing in theaters from the Movie Database API.

#### BONUS
- [x] (2pts) Views should be responsive for both landscape/portrait mode.
   - [x] (1pt) In portrait mode, the poster image, title, and movie overview is shown.
   - [x] (1pt) In landscape mode, the rotated alternate layout should use the backdrop image instead and show the title and movie overview to the right of it.

- [x] (2pts) Display a nice default [placeholder graphic](https://guides.codepath.org/android/Displaying-Images-with-the-Glide-Library#advanced-usage) for each image during loading
- [x] (2pts) Improved the user interface by experimenting with styling and coloring.
- [x] (2pts) For popular movies (i.e. a movie voted for more than 5 stars), the full backdrop image is displayed. Otherwise, a poster image, the movie title, and overview is listed. Use Heterogenous RecyclerViews and use different ViewHolder layout files for popular movies and less popular ones.

### App Walkthough GIF

Portrait view (movies with a rating above 7.0/10.0 are featured):

<img src="https://github.com/Thomas-McKanna/Flixster/raw/master/flixter1.gif" width=250><br>

Landscape view:

<img src="https://github.com/Thomas-McKanna/Flixster/raw/master/flixter2.gif" width=500><br>

### Notes

1. To use the full range of GlideApp features I had to make sure to use these dependencies for compatibility with androidx:

```
 implementation 'com.github.bumptech.glide:glide:4.10.0'
 annotationProcessor 'com.github.bumptech.glide:compiler:4.10.0'
```

2. The https://github.com/wasabeef/recyclerview-animators repository has animations that can be applied as the user scrolls through a RecyclerView. To use it, cast your custom RecyclerView adapter to one of the adapter animation classes.

### Open-source libraries used

- [Android Async HTTP](https://github.com/codepath/CPAsyncHttpClient) - Simple asynchronous HTTP requests with JSON parsing
- [Glide](https://github.com/bumptech/glide) - Image loading and caching library for Androids
