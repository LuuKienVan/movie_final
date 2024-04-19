package com.example.movie.ui.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.movie.adapters.OrderAdapter;
import com.example.movie.databinding.FragmentOrderBinding;
import com.example.movie.models.OrderItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class OrderFragment extends Fragment {

    private FragmentOrderBinding binding;
    OrderAdapter orderAdapter;
    ArrayList<OrderItem> orderList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentOrderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        orderList = new ArrayList();

        FirebaseFirestore.getInstance()
                .collection("Users_Tickets").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot i: queryDocumentSnapshots) {
                            orderList.add(new OrderItem(
                                    i.getData().get("movie_id").toString(),
                                    i.getData().get("banner_image").toString(),
                                    i.getData().get("movie_name").toString(),
                                    i.getData().get("cinema_name").toString(),
                                    i.getData().get("cinema_location").toString(),
                                    i.getData().get("quantity").toString(),
                                    i.getData().get("price").toString(),
                                    i.getData().get("total_price").toString(),
                                    i.getData().get("date").toString(),
                                    i.getData().get("time").toString(),
                                    i.getData().get("seat_no").toString()
                            ));
                        }
                        orderAdapter = new OrderAdapter(binding.getRoot().getContext(), orderList);
                        binding.orderRecyclerView.setAdapter(orderAdapter);
                        orderAdapter.notifyDataSetChanged();
                    }
                });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}