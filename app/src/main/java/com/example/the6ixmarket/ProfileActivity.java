package com.example.the6ixmarket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button profileDetailsButton = findViewById(R.id.profile_details_button);
        Button createListingButton = findViewById(R.id.create_listing_button);
        Button messagesButton = findViewById(R.id.messages_button);
        Button activeListingsButton = findViewById(R.id.active_listings_button);
        Button soldListingsButton = findViewById(R.id.sold_listings_button);
        Button backButton = findViewById(R.id.back_button);

        profileDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ProfileDetailsActivity.class);
                startActivity(intent);
            }
        });

        createListingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, CreateListingActivity.class);
                startActivity(intent);
            }
        });

        messagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MessagesActivity.class);
                startActivity(intent);
            }
        });

        activeListingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ActiveListingsActivity.class);
                startActivity(intent);
            }
        });

        soldListingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, SoldListingsActivity.class);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
