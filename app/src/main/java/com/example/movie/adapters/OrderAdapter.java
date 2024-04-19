package com.example.movie.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movie.R;
import com.example.movie.adapters.ViewHolder.BindingViewHolder;
import com.example.movie.databinding.ViewOrderLayoutBinding;
import com.example.movie.models.OrderItem;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<BindingViewHolder>{

    Context context;
    ArrayList<OrderItem> orderList;

    public OrderAdapter(Context context, ArrayList<OrderItem> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public BindingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewOrderLayoutBinding _view = ViewOrderLayoutBinding.inflate(LayoutInflater.from(this.context), parent, false);
        return new BindingViewHolder(_view);
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder holder, int position) {
        OrderItem order = orderList.get(position);
        ViewOrderLayoutBinding itemBinding = (ViewOrderLayoutBinding) holder.getItemBinding();
        Glide.with(context)
                .load(order.banner_image)
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_image_24)
                .into(itemBinding.roundShapeImageview);
        itemBinding.txtCinemaLocation.setText("Location: " + order.cinemaLocation);
        itemBinding.txtCinemaName.setText("Cinema Name: " + order.cinemaName);
        itemBinding.txtMovieName.setText("Movie Name: " + order.movie_name);
        itemBinding.txtDate.setText("Date: " + order.date);
        itemBinding.txtTime.setText("Time: " + order.time);
        itemBinding.txtseat.setText("Seat No: " + order.seat);
        itemBinding.txtPrice.setText("Price: " + order.price);
        itemBinding.txtQuantity.setText("Quantity: " + order.quantity);
        itemBinding.txtTotalPrice.setText("Total Price: " + order.totalPrice);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}

