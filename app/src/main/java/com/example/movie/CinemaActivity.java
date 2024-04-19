package com.example.movie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.movie.adapters.CinemaAdapter;
import com.example.movie.databinding.ActivityCinemaBinding;
import com.example.movie.models.Cinema;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CinemaActivity extends AppCompatActivity {
    ActivityCinemaBinding binding;
    ArrayList<Cinema> cinemaList;
    ArrayList<Cinema> selectedCinemaList;
    CinemaAdapter cinemaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cinema);
        setSupportActionBar(binding.toolbar);

        Intent intent = getIntent();

        Cinema cinema1 = new Cinema();
        cinema1.cinemaId = "HCM1";
        cinema1.cinemaName = "CGV Hung Vuong";
        cinema1.cinemaLocation = "HCM";
        cinema1.cinemaDrawable = "https://lh3.googleusercontent.com/p/AF1QipOzHkryNtKb2lEJxtW0FtilGdAxKJSBGKcVyw87=s680-w680-h510";
        Cinema cinema2 = new Cinema();
        cinema2.cinemaId = "HN1";
        cinema2.cinemaName = "CGV Ha Noi Centerpoint";
        cinema2.cinemaLocation = "HN";
        cinema2.cinemaDrawable = "https://lh5.googleusercontent.com/p/AF1QipOFJqpaGz-wEQVqM0CZuCsN2rajPzLptoNr9QJm=w141-h101-n-k-no-nu";
        cinemaList = new ArrayList<Cinema>();
        cinemaList.add(cinema1);
        cinemaList.add(cinema2);

        String movie_name = intent.getStringExtra("movie_name");
        String banner_image = intent.getStringExtra("banner_image");
        String movieId = intent.getStringExtra("movieId");
        SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.app_name),
                MODE_PRIVATE
        );

        selectedCinemaList = new ArrayList<Cinema>();
        String locationStr = sharedPreferences.getString("location_str", null);
        Log.d("LocationStr", locationStr);
        if (locationStr != null) {
            if (locationStr.length() != 0) {
                List<String> lstValues = Arrays.stream(locationStr.split(",")).map(l -> l.trim()).collect(Collectors.toList());
                for(int i = 0; i < lstValues.size(); i++) {
                    for(int j = 0; j < cinemaList.size(); j++) {
                        if (cinemaList.get(j).cinemaLocation.equals(lstValues.get(i))) {
                            selectedCinemaList.add(cinemaList.get(j));
                            Log.d("SelectedCinema", cinemaList.get(j).cinemaLocation);
                        }
                    }
                }
            }
        }

        if (selectedCinemaList.size() != 0){
            cinemaAdapter = new CinemaAdapter(
                    this, selectedCinemaList, movie_name, banner_image, movieId
            );
            binding.movieRecyclerView.setAdapter(cinemaAdapter);
        } else{
            cinemaAdapter = new CinemaAdapter(
                    this, cinemaList, movie_name, banner_image, movieId
            );
            binding.movieRecyclerView.setAdapter(cinemaAdapter);
        }

    }
}