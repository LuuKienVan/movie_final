package com.example.movie.ui.home;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.movie.R;
import com.example.movie.adapters.MovieAdapter;
import com.example.movie.databinding.FragmentHomeBinding;
import com.example.movie.models.MovieItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    MovieAdapter movieAdapter;
    ArrayList<MovieItem> movieList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        movieList = new ArrayList<>();
        retrieveMovie(binding);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void retrieveMovie(FragmentHomeBinding binding) {
        FirebaseFirestore.getInstance().collection("Movie").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int count = 0;
                    for (QueryDocumentSnapshot i : task.getResult()) {
                        count += 1;
                        boolean stated;
                        if (i.get("stated") != null) {
                            stated = (boolean) i.get("stated");
                        } else {
                            stated = false;
                        }
                        movieList.add(
                                new MovieItem(
                                        Integer.toString(count),
                                        i.get("about_movie").toString(),
                                        i.get("banner_image").toString(),
                                        i.get("cover_image").toString(),
                                        i.get("languages").toString(),
                                        i.get("movie_duration").toString(),
                                        i.get("movie_name").toString(),
                                        i.get("rating").toString(),
                                        i.get("release_date").toString(),
                                        stated
                                )
                        );
                    }
                    movieAdapter = new MovieAdapter(binding.getRoot().getContext(), movieList);
                    binding.movieRecyclerView.setAdapter(movieAdapter);
                    movieAdapter.notifyDataSetChanged();
                } else {
                    Log.d("RetrieveError", "Error getting documents: ", task.getException());
                }
            }
        });
    }
}