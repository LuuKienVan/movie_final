package com.example.movie.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie.BR;
import com.example.movie.MovieDetailsActivity;
import com.example.movie.adapters.ViewHolder.BindingViewHolder;
import com.example.movie.databinding.ViewMovieLayoutBinding;
import com.example.movie.models.MovieItem;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<BindingViewHolder> {

    private  Context context;
    private ArrayList<MovieItem> movieList;

    public MovieAdapter(Context context, ArrayList<MovieItem> movieList) {

        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public BindingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewMovieLayoutBinding _view = ViewMovieLayoutBinding.inflate(LayoutInflater.from(this.context), parent, false);
        return new BindingViewHolder(_view);
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder holder, int position) {
        MovieItem movie = (MovieItem) this.movieList.get(position);
        holder.getItemBinding().setVariable(BR.movieItemClient, movie);
        holder.itemView.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public void onClick(View it) {
                Intent intent = new Intent(MovieAdapter.this.context, MovieDetailsActivity.class);
                intent.putExtra("movieId", movie.movieId);
                intent.putExtra("about_movie", movie.about_movie);
                intent.putExtra("banner_image", movie.banner_image);
                intent.putExtra("cover_image", movie.cover_image);
                intent.putExtra("languages", movie.languages);
                intent.putExtra("movie_duration", movie.movie_duration);
                intent.putExtra("movie_name", movie.movie_name);
                intent.putExtra("rating", movie.rating);
                intent.putExtra("release_date", movie.release_date);
                intent.putExtra("trailer", movie.trailer);
                intent.putExtra("actors", movie.actors);
                intent.putExtra("genre", movie.genre);
                MovieAdapter.this.context.startActivity(intent);
            }
        }));
        holder.getItemBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

}


