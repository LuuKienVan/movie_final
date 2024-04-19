package com.example.movie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.movie.adapters.LocationAdapter;
import com.example.movie.databinding.ActivityLocationBinding;
import com.example.movie.models.City;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class LocationActivity extends AppCompatActivity {
    ArrayList<City> courseList;
    LocationAdapter locationAdapter;
    ActivityLocationBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_location);
        setSupportActionBar(binding.toolbar);

        SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.app_name),
                MODE_PRIVATE
        );

        City hcm = new City();
        hcm.cityId = "HCM";
        hcm.city = "HCM";
        //hcm.isChecked = true;
        City hn = new City();
        hn.cityId = "HN";
        hn.city = "HN";
        //hn.isChecked = true;
        courseList = new ArrayList<City>();
        courseList.add(hcm);
        courseList.add(hn);

        String locationStr = sharedPreferences.getString("location_str", null);
        if (locationStr != null) {
            if (locationStr.length() != 0) {
                List<String> lstValues = Arrays.stream(locationStr.split(",")).map(l -> l.trim()).collect(Collectors.toList());
                for(int i = 0; i < lstValues.size(); i++) {
                    for(int j = 0; j < courseList.size(); j++) {
                        if (courseList.get(j).city.equals(lstValues.get(i))) {
                            courseList.get(j).isChecked = true;
                        }
                    }
                }
            }
        }

        locationAdapter = new LocationAdapter(this, courseList);
        binding.locationRecyclerView.setAdapter(locationAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem search = menu.findItem(R.id.app_bar_search);

        SearchView searchView = (SearchView) search.getActionView();
        searchView.setMaxWidth(android.R.attr.width);
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<City> checkItemModelArrayList = new ArrayList();
                for (City i: courseList) {
                    if (i.city.toLowerCase(Locale.getDefault())
                            .contains(newText.toLowerCase((Locale.getDefault())))) {
                        checkItemModelArrayList.add(i);
                    }
                }
                locationAdapter = new LocationAdapter(
                        binding.getRoot().getContext(), checkItemModelArrayList
                );
                binding.locationRecyclerView.setAdapter(locationAdapter);
                locationAdapter.notifyDataSetChanged();
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}