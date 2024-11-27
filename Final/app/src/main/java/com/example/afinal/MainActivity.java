package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.afinal.ui.AccountManagementActivity;
import com.example.afinal.ui.ExploreListingsActivity;
import com.example.afinal.ui.MarketManagementActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize buttons
        Button buttonAccountManagement = findViewById(R.id.buttonAccountManagement);
        Button buttonMarketManagement = findViewById(R.id.buttonMarketManagement);
        Button buttonExploreListings = findViewById(R.id.buttonExploreListings);

        // Navigate to Account Management Activity
        buttonAccountManagement.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AccountManagementActivity.class);
            startActivity(intent);
        });

        // Navigate to Market Management Activity
        buttonMarketManagement.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MarketManagementActivity.class);
            startActivity(intent);
        });

        // Navigate to Explore Listings Activity
        buttonExploreListings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ExploreListingsActivity.class);
            startActivity(intent);
        });
    }
}
