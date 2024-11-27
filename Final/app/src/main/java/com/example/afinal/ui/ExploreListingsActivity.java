package com.example.afinal.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.afinal.R;
import com.example.afinal.database.ListingRepository;
import com.example.afinal.model.Listing;

import java.util.List;

public class ExploreListingsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListingAdapter listingAdapter;
    private ListingRepository listingRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_listings);

        recyclerView = findViewById(R.id.listingsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listingRepository = new ListingRepository(this);
        List<Listing> listings = listingRepository.getAllListings();

        listingAdapter = new ListingAdapter(this, listings, listing ->
                Toast.makeText(this, "Clicked: " + listing.getTitle(), Toast.LENGTH_SHORT).show()
        );
        recyclerView.setAdapter(listingAdapter);
    }
}
