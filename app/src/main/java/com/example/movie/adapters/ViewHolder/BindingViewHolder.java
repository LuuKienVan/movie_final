package com.example.movie.adapters.ViewHolder;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class BindingViewHolder extends RecyclerView.ViewHolder {
    @NotNull
    private final ViewDataBinding itemBinding;
    @NotNull
    public final ViewDataBinding getItemBinding() {
        return this.itemBinding;
    }

    public BindingViewHolder(@NotNull ViewDataBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
    }
}