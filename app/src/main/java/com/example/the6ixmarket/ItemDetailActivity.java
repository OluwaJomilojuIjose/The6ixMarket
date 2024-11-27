package com.example.the6ixmarket;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class ItemDetailActivity extends AppCompatActivity {

    // Existing variables
    TextView itemTitleTextView, itemPriceTextView, itemDescriptionTextView, itemSellerTextView;
    ImageView itemImageView;
    Button messageSellerButton;
    Button markAsSoldButton, backButton; // New button

    TextView itemCountryTextView, itemPostalCodeTextView;

    private DatabaseHelper databaseHelper;
    private String itemId; // To identify the listing

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
        markAsSoldButton = findViewById(R.id.mark_as_sold_button); // New

        itemCountryTextView = findViewById(R.id.item_detail_country);
        itemPostalCodeTextView = findViewById(R.id.item_detail_postal_code);

        databaseHelper = new DatabaseHelper(this);

        // Get data from intent
        Intent intent = getIntent();
        itemId = intent.getStringExtra("ITEM_ID"); // Get item ID
        String title = intent.getStringExtra("ITEM_TITLE");
        String description = intent.getStringExtra("ITEM_DESCRIPTION");
        String imageUri = intent.getStringExtra("ITEM_IMAGE");
        String price = intent.getStringExtra("ITEM_PRICE");
        String seller = intent.getStringExtra("ITEM_SELLER");
        String country = intent.getStringExtra("ITEM_COUNTRY");
        String postalCode = intent.getStringExtra("ITEM_POSTAL_CODE");
        String status = intent.getStringExtra("ITEM_STATUS");

        // Set data to views
        itemTitleTextView.setText(title);
        itemDescriptionTextView.setText(description);
        itemPriceTextView.setText(price);
        itemSellerTextView.setText(seller);
        itemCountryTextView.setText("Country: " + country);
        itemPostalCodeTextView.setText("Postal Code: " + postalCode);

        if (imageUri != null && !imageUri.isEmpty()) {
            itemImageView.setImageURI(Uri.parse(imageUri));
        } else {
            itemImageView.setImageResource(R.drawable.placeholder_image);
        }

        // Hide the "Mark as Sold" button if already sold
        if ("sold".equalsIgnoreCase(status)) {
            markAsSoldButton.setVisibility(View.GONE);
        }

        // Set up the button click listener
        messageSellerButton.setOnClickListener(view -> {
            // Redirect to MessagesActivity or any other action
            Intent messageIntent = new Intent(ItemDetailActivity.this, MessagesActivity.class);
            startActivity(messageIntent);
        });

        // Mark as Sold button listener
        markAsSoldButton.setOnClickListener(view -> {
            markListingAsSold(itemId);
        });

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void markListingAsSold(String itemId) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_STATUS, "sold");

        int rowsAffected = db.update(DatabaseHelper.TABLE_LISTINGS, values, DatabaseHelper.COLUMN_ID + "=?", new String[]{itemId});
        if (rowsAffected > 0) {
            Toast.makeText(this, "Listing marked as sold.", Toast.LENGTH_SHORT).show();
            markAsSoldButton.setVisibility(View.GONE);
        } else {
            Toast.makeText(this, "Failed to update listing.", Toast.LENGTH_SHORT).show();
        }
    }
}
