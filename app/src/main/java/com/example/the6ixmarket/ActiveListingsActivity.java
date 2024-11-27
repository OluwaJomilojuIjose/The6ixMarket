package com.example.the6ixmarket;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ActiveListingsActivity extends AppCompatActivity {

    private static final String TAG = "ActiveListingsActivity";

    RecyclerView recyclerView;
    DatabaseHelper databaseHelper;
    ItemAdapter itemAdapter;
    ArrayList<Item> itemList;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_listings);

        recyclerView = findViewById(R.id.active_listings_recycler_view);
        databaseHelper = new DatabaseHelper(this);

        itemList = new ArrayList<>();
        itemAdapter = new ItemAdapter(this, itemList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(itemAdapter);

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
            String selection = DatabaseHelper.COLUMN_STATUS + "=?";
            String[] selectionArgs = {"active"};

            cursor = db.query(DatabaseHelper.TABLE_LISTINGS, null, selection, selectionArgs, null, null, null);

            if (cursor != null) {
                Log.d(TAG, "Cursor count: " + cursor.getCount());
                while (cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
                    String price = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRICE));
                    String imageUri = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMAGE_URI));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION));
                    String seller = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SELLER));
                    String country = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COUNTRY));
                    String postalCode = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_POSTAL_CODE));
                    String status = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STATUS));

                    if (description == null) description = "No description";
                    if (seller == null) seller = "Unknown Seller";

                    Item item = new Item(id, title, description, imageUri, price, seller, country, postalCode, status);
                    itemList.add(item);

                    Log.d(TAG, "Loaded active item: " + title);
                }
            } else {
                Log.d(TAG, "Cursor is null");
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
