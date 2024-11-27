package com.example.the6ixmarket;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ActiveListingsActivity extends AppCompatActivity {

    private static final String TAG = "ActiveListingsActivity";

    RecyclerView recyclerView;
    DatabaseHelper databaseHelper;
    ItemAdapter itemAdapter;
    ArrayList<Item> itemList;

    private FirebaseAuth mAuth; // Firebase Authentication

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_listings);

        recyclerView = findViewById(R.id.active_listings_recycler_view);
        databaseHelper = new DatabaseHelper(this);
        mAuth = FirebaseAuth.getInstance();

        itemList = new ArrayList<>();
        itemAdapter = new ItemAdapter(this, itemList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(itemAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadActiveListings();
    }

    private void loadActiveListings() {
        itemList.clear();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                String sellerId = currentUser.getUid();
                String selection = DatabaseHelper.COLUMN_STATUS + "=? AND " + DatabaseHelper.COLUMN_SELLER_ID + "=?";
                String[] selectionArgs = {"active", sellerId};

                cursor = db.query(DatabaseHelper.TABLE_LISTINGS, null, selection, selectionArgs, null, null, null);

                if (cursor != null) {
                    Log.d(TAG, "Cursor count: " + cursor.getCount());
                    while (cursor.moveToNext()) {
                        // Fetch data from cursor
                        String id = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                        String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
                        String price = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRICE));
                        String imageUri = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMAGE_URI));
                        String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION));
                        String seller = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SELLER));
                        String country = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COUNTRY));
                        String postalCode = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_POSTAL_CODE));
                        String status = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STATUS));
                        String itemSellerId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SELLER_ID));

                        // Handle null values if necessary
                        if (description == null) description = "No description";
                        if (seller == null) seller = "Unknown Seller";

                        // Create Item object
                        Item item = new Item(id, title, description, imageUri, price, seller, itemSellerId, country, postalCode, status);
                        itemList.add(item);
                    }
                }
            } else {
                // Handle case when user is not logged in
                Log.d(TAG, "User not logged in");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading active listings: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        itemAdapter.notifyDataSetChanged();
        Log.d(TAG, "Active listings loaded. Total items: " + itemList.size());
    }
}
