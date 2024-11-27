package com.example.the6ixmarket;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ItemDetailActivity extends AppCompatActivity {

    // Existing variables
    TextView itemTitleTextView, itemPriceTextView, itemDescriptionTextView, itemSellerTextView;
    ImageView itemImageView;
    Button messageSellerButton;

    // New variables
    TextView itemCountryTextView, itemPostalCodeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        // Initialize views
        itemTitleTextView = findViewById(R.id.item_detail_title);
        itemPriceTextView = findViewById(R.id.item_detail_price);
        itemDescriptionTextView = findViewById(R.id.item_detail_description);
        itemSellerTextView = findViewById(R.id.item_detail_seller);
        itemImageView = findViewById(R.id.item_detail_image);
        messageSellerButton = findViewById(R.id.message_seller_button);

        itemCountryTextView = findViewById(R.id.item_detail_country); // New
        itemPostalCodeTextView = findViewById(R.id.item_detail_postal_code); // New

        // Get data from intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("ITEM_TITLE");
        String description = intent.getStringExtra("ITEM_DESCRIPTION");
        String imageUri = intent.getStringExtra("ITEM_IMAGE");
        String price = intent.getStringExtra("ITEM_PRICE");
        String seller = intent.getStringExtra("ITEM_SELLER");
        String country = intent.getStringExtra("ITEM_COUNTRY"); // New
        String postalCode = intent.getStringExtra("ITEM_POSTAL_CODE"); // New

        // Set data to views
        itemTitleTextView.setText(title);
        itemDescriptionTextView.setText(description);
        itemPriceTextView.setText(price);
        itemSellerTextView.setText(seller);
        itemCountryTextView.setText("Country: " + country); // New
        itemPostalCodeTextView.setText("Postal Code: " + postalCode); // New

        if (imageUri != null && !imageUri.isEmpty()) {
            itemImageView.setImageURI(Uri.parse(imageUri));
        } else {
            itemImageView.setImageResource(R.drawable.placeholder_image);
        }

        // Set up the button click listener
        messageSellerButton.setOnClickListener(view -> {
            // Redirect to MessagesActivity or any other action
            Intent messageIntent = new Intent(ItemDetailActivity.this, MessagesActivity.class);
            startActivity(messageIntent);
        });
    }
}
