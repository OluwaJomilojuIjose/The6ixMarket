package com.example.the6ixmarket;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

// Add the missing import statement
import com.google.firebase.auth.FirebaseAuth;

public class ItemDetailActivity extends AppCompatActivity {

    TextView itemTitleTextView, itemPriceTextView, itemDescriptionTextView, itemSellerTextView;
    ImageView itemImageView;
    Button messageSellerButton;
    Button markAsSoldButton;

    TextView itemCountryTextView, itemPostalCodeTextView;

    private DatabaseHelper databaseHelper;
    private String itemId; // To identify the listing
    private String sellerId; // Seller's UID

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
        markAsSoldButton = findViewById(R.id.mark_as_sold_button);

        itemCountryTextView = findViewById(R.id.item_detail_country);
        itemPostalCodeTextView = findViewById(R.id.item_detail_postal_code);

        databaseHelper = new DatabaseHelper(this);

        // Get data from intent
        Intent intent = getIntent();
        itemId = intent.getStringExtra("ITEM_ID");
        String title = intent.getStringExtra("ITEM_TITLE");
        String description = intent.getStringExtra("ITEM_DESCRIPTION");
        String imageUri = intent.getStringExtra("ITEM_IMAGE");
        String price = intent.getStringExtra("ITEM_PRICE");
        String seller = intent.getStringExtra("ITEM_SELLER");
        sellerId = intent.getStringExtra("ITEM_SELLER_ID"); // Get sellerId
        String country = intent.getStringExtra("ITEM_COUNTRY");
        String postalCode = intent.getStringExtra("ITEM_POSTAL_CODE");
        String status = intent.getStringExtra("ITEM_STATUS");

        // Set data to views
        itemTitleTextView.setText(title);
        itemDescriptionTextView.setText(description);
        itemPriceTextView.setText(price);
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

        // Load seller details
        loadSellerDetails(sellerId);

        // Set up the button click listener
        messageSellerButton.setOnClickListener(view -> {
            String buyerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String conversationId;
            if (buyerId.compareTo(sellerId) < 0) {
                conversationId = buyerId + "_" + sellerId;
            } else {
                conversationId = sellerId + "_" + buyerId;
            }

            Intent chatIntent = new Intent(ItemDetailActivity.this, ChatActivity.class);
            chatIntent.putExtra("CONVERSATION_ID", conversationId);
            chatIntent.putExtra("OTHER_USER_ID", sellerId);
            startActivity(chatIntent);
        });

        // Mark as Sold button listener
        markAsSoldButton.setOnClickListener(view -> {
            markListingAsSold(itemId);
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

    private void loadSellerDetails(String sellerId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            String selection = DatabaseHelper.COLUMN_UID + "=?";
            String[] selectionArgs = {sellerId};

            cursor = db.query(DatabaseHelper.TABLE_USER, null, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                String sellerName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
                String sellerEmail = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMAIL));

                itemSellerTextView.setText(sellerName);
                // Optionally display email or other details
            } else {
                itemSellerTextView.setText("Unknown Seller");
            }
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
