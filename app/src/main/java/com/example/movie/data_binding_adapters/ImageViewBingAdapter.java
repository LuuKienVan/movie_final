package com.example.movie.data_binding_adapters;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.example.movie.R;

import org.jetbrains.annotations.NotNull;

public class ImageViewBingAdapter {
    @BindingAdapter({"imageUrl"})
    public static void loadImageFromInternet(@NotNull ImageView view, @NotNull String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_image_24)
                .into(view);
    }
}
