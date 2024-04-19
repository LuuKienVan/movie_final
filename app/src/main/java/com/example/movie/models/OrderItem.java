package com.example.movie.models;

public class OrderItem {

    public String orderId;

    public String movieId;

    public String userId;

    public String banner_image;

    public String movie_name;

    public String cinemaName;

    public String cinemaLocation;

    public String quantity;

    public String price;

    public String totalPrice;

    public String date;

    public String time;

    public String seat;

    public String quality;

    public OrderItem(String id, String bannerImage, String movieName, String cinemaName, String cinemaLocation, String quantity, String price, String totalPrice, String date, String time, String seatingNo) {
        this.movieId = id;
        this.movie_name = movieName;
        this.banner_image = bannerImage;
        this.cinemaName = cinemaName;
        this.cinemaLocation = cinemaLocation;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
        this.date = date;
        this.time = time;
        this.seat = seatingNo;
    }
}
