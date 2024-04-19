package com.example.movie.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movie.R;
import com.example.movie.SeatingActivity;
import com.example.movie.adapters.ViewHolder.BindingViewHolder;
import com.example.movie.databinding.ViewCinemaLayoutBinding;
import com.example.movie.models.Cinema;


import java.util.ArrayList;

public class CinemaAdapter extends  RecyclerView.Adapter<BindingViewHolder>{
    Context context;
    ArrayList<Cinema> cinemaList;
    String movieName;
    String banner_image;
    String movieId;

//    private ArrayList<Integer> price = new ArrayList<Integer>() {{
//        add(5);
//        add(5);
//        add(7);
//        add(10);
//        add(10);
//        add(7);
//        add(5);
//        add(5);
//    }};

    public CinemaAdapter(
            Context context,
            ArrayList<Cinema> cinemaList,
            String movieName,
            String banner_image,
            String movieId
    ) {
        this.context = context;
        this.cinemaList = cinemaList;
        this.movieName = movieName;
        this.banner_image = banner_image;
        this.movieId = movieId;
    }

    @NonNull
    @Override
    public BindingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewCinemaLayoutBinding _view = ViewCinemaLayoutBinding.inflate(LayoutInflater.from(this.context), parent, false);
        return new BindingViewHolder(_view);
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Cinema cinema = cinemaList.get(position);
        ViewCinemaLayoutBinding itemBinding = (ViewCinemaLayoutBinding) holder.getItemBinding();
        Glide.with(context)
                .load(cinema.cinemaDrawable)
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_image_24)
                .into(itemBinding.ivGroupFirstImg);
        itemBinding.txtCinemaName.setText(cinema.cinemaName);
        itemBinding.txtCinemaLocation.setText(cinema.cinemaLocation);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SeatingActivity.class);
                intent.putExtra("movie_name", movieName);
                intent.putExtra("movieId", movieId);
                intent.putExtra("price", 0);
                intent.putExtra("banner_image", banner_image);
                intent.putExtra("cinema_name", cinema.cinemaName);
                intent.putExtra("cinema_location", cinema.cinemaLocation);
                intent.putExtra("cinema_drawable", cinema.cinemaDrawable);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cinemaList.size();
    }
}
