package com.example.management;

import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.management.model.Listing;

import java.util.ArrayList;

public class PurchasedListingsActivity extends AppCompatActivity {

    private ListView listView;
    private ListingAdapter listingAdapter;
    private ArrayList<Listing> purchasedListings; // Assume Listing is a model class representing an item

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchased_listings);

        listView = findViewById(R.id.listView);

        // Sample data; in a real app, you'd fetch this from a database
        purchasedListings = new ArrayList<>();
        purchasedListings.add(new Listing("Purchased Item 1", "Description 1", 30.0));
        purchasedListings.add(new Listing("Purchased Item 2", "Description 2", 40.0));

        listingAdapter = new ListingAdapter(this, purchasedListings);
        listView.setAdapter(listingAdapter);
    }
}

