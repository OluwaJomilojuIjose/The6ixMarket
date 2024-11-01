package com.example.management;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.management.model.Listing;

import java.util.ArrayList;

public class SavedListingsActivity extends AppCompatActivity {

    private ListView listView;
    private ListingAdapter listingAdapter;
    private ArrayList<Listing> savedListings; // Assume Listing is a model class representing an item

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_listings);

        listView = findViewById(R.id.listView);

        Button buttonBack = findViewById(R.id.backButton);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Sample data; in a real app, you'd fetch this from a database
        savedListings = new ArrayList<>();
        savedListings.add(new Listing("Saved Item 1", "Description 1", 15.0));
        savedListings.add(new Listing("Saved Item 2", "Description 2", 25.0));

        listingAdapter = new ListingAdapter(this, savedListings);
        listView.setAdapter(listingAdapter);
    }
}

