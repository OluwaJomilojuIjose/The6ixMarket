package com.example.management;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.management.model.Listing;

import java.util.ArrayList;

public class ManageListingsActivity extends AppCompatActivity {

    private ListView listView;
    private ListingAdapter listingAdapter;
    private ArrayList<Listing> listings; // Assume Listing is a model class representing an item

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_listings);

        listView = findViewById(R.id.listView);
        Button buttonAddListing = findViewById(R.id.buttonAddListing);

        // Sample data; in a real app, you'd fetch this from a database
        listings = new ArrayList<>();
        listings.add(new Listing("Item 1", "Description 1", 10.0));
        listings.add(new Listing("Item 2", "Description 2", 20.0));

        listingAdapter = new ListingAdapter(this, listings);
        listView.setAdapter(listingAdapter);

        // Add Listing button listener
        buttonAddListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageListingsActivity.this, AddEditListingActivity.class);
                startActivity(intent);
            }
        });
    }
}
