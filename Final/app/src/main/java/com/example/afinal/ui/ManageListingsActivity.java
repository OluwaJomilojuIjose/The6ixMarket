package com.example.afinal.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.afinal.R;

public class ManageListingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_listings);

        // Initialize buttons
        Button addListingButton = findViewById(R.id.buttonAddListing);

        // Set listener for adding listings
        addListingButton.setOnClickListener(v -> {
            Intent intent = new Intent(ManageListingsActivity.this, AddEditListingActivity.class);
            startActivity(intent);
        });
    }
}
