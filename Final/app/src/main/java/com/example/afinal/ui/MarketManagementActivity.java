package com.example.afinal.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.afinal.R;

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

        // Set up button listeners
        buttonManageListings.setOnClickListener(v -> startActivity(new Intent(this, ManageListingsActivity.class)));
        buttonSaveListings.setOnClickListener(v -> startActivity(new Intent(this, SavedListingsActivity.class)));
        buttonPurchasedListings.setOnClickListener(v -> startActivity(new Intent(this, PurchasedListingsActivity.class)));
        buttonBack.setOnClickListener(v -> finish());
    }
}
