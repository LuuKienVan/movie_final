package com.example.movie.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie.BR;
import com.example.movie.R;
import com.example.movie.adapters.ViewHolder.BindingViewHolder;
import com.example.movie.databinding.CitiesItemLayoutBinding;
import com.example.movie.models.City;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<BindingViewHolder> {

    private Context context;
    private ArrayList<City> cities;

    public LocationAdapter(Context context, ArrayList<City> cities) {
        this.context = context;
        this.cities = cities;
    }

    @NonNull
    @Override
    public BindingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CitiesItemLayoutBinding _view = CitiesItemLayoutBinding.inflate(LayoutInflater.from(this.context), parent, false);
        return new BindingViewHolder(_view);
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder holder, @SuppressLint("RecyclerView") int position) {
        City city = this.cities.get(position);
        holder.getItemBinding().setVariable(BR.locationItem, city);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cities.get(position).isChecked = !cities.get(position).isChecked;
                SharedPreferences sharedPreferences = context.getSharedPreferences(
                        context.getString(R.string.app_name),
                        AppCompatActivity.MODE_PRIVATE
                );
                StringBuilder locationStr = new StringBuilder();
                locationStr.delete(0, locationStr.length());
                for(City i: cities) {
                    if (i.isChecked) {
                        locationStr.append(i.city + ",");
                    }
                }
                if (locationStr.length() != 0) {
                    locationStr.deleteCharAt(locationStr.length() - 1);
                    sharedPreferences.edit().putString("location_str", locationStr.toString()).apply();
                }
                notifyItemChanged(position);
            }
        });

        holder.getItemBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }
}
