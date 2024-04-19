package com.example.movie.models;

public class MovieItem {
    public String movieId;
    public String movie_name;

    public String about_movie;

    public String banner_image;

    public String cover_image;

    public String languages;

    public String movie_duration;

    public String rating;

    public String release_date;

    public String trailer;

    public String actors;

    public String genre;

    public boolean stated = false;


    public MovieItem(String movieId, String aboutMovie, String bannerImage, String coverImage, String languages, String movieDuration, String movieName, String rating, String releaseDate, boolean stated) {
        this.movieId = movieId;
        this.about_movie = aboutMovie;
        this.banner_image = bannerImage;
        this.cover_image = coverImage;
        this.languages = languages;
        this.movie_duration = movieDuration;
        this.movie_name = movieName;
        this.rating = rating;
        this.release_date = releaseDate;
        this.stated = stated;
    }
}
