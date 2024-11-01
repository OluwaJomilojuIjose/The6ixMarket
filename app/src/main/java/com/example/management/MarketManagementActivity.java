package com.example.management;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MarketManagementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_management);

        // Initialize buttons
        Button buttonManageListings = findViewById(R.id.buttonManageListings);
        Button buttonSaveListings = findViewById(R.id.buttonSaveListings);
        Button buttonPurchasedListings = findViewById(R.id.buttonPurchasedListings);

        Button buttonBack = findViewById(R.id.backButton);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Set onClick listeners for each button
        buttonManageListings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Manage Listings Activity
                Intent intent = new Intent(MarketManagementActivity.this, ManageListingsActivity.class);
                startActivity(intent);
            }
        });

        buttonSaveListings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Saved Listings Activity
                Intent intent = new Intent(MarketManagementActivity.this, SavedListingsActivity.class);
                startActivity(intent);
            }
        });

        buttonPurchasedListings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Purchased Listings Activity
                Intent intent = new Intent(MarketManagementActivity.this, PurchasedListingsActivity.class);
                startActivity(intent);
            }
        });

    }
}

