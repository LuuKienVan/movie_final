package com.example.movie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.movie.databinding.ActivityMovieDetailsBinding;

public class MovieDetailsActivity extends AppCompatActivity {

    ActivityMovieDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);

        Intent intent = getIntent();

        Glide.with(this)
                .load(intent.getStringExtra("cover_image"))
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_image_24)
                .into(binding.coverImage);

        String releaseTxt = "Release Date: " +
                intent.getStringExtra("release_date") + "\nTime Duration: " +
                intent.getStringExtra("movie_duration");
        String ratingTxt = intent.getStringExtra("rating") +"%";
        String langTxt = "Languages: "+ intent.getStringExtra("languages");
        String banner_image = intent.getStringExtra("banner_image");
        String movieId = intent.getStringExtra("movieId");

        binding.movieNameTxt.setText(intent.getStringExtra("movie_name"));
        binding.movieDescTxt.setText(intent.getStringExtra("about_movie"));
        binding.ratingTxt.setText(ratingTxt);
        binding.releaseTxt.setText(releaseTxt);
        binding.langTxt.setText(langTxt);

        binding.bookTicketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intnt = new Intent(getApplicationContext(), CinemaActivity.class);
                intnt.putExtra("movie_name",binding.movieNameTxt.getText().toString());
                intnt.putExtra( "banner_image",banner_image);
                intnt.putExtra( "movieId",movieId);
                startActivity(intnt);
            }
        });
    }
}