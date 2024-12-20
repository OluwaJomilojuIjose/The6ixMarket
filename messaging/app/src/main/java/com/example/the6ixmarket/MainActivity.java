package com.example.the6ixmarket;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable; // Added import
import android.text.TextWatcher; // Added import
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    Button logoutButton;
    ImageButton profileButton;
    TextView userDetails;
    RecyclerView recyclerView;
    EditText searchBar;
    DatabaseHelper databaseHelper;
    ItemAdapter itemAdapter;
    ArrayList<Item> itemList;
    ArrayList<Item> filteredList;

    private FirebaseAuth mAuth; // Firebase Authentication

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        logoutButton = findViewById(R.id.logout);
        profileButton = findViewById(R.id.profile_button);
        userDetails = findViewById(R.id.user_details);
        recyclerView = findViewById(R.id.recycler_view);
        searchBar = findViewById(R.id.search_bar);

        databaseHelper = new DatabaseHelper(this);

        itemList = new ArrayList<>();
        filteredList = new ArrayList<>();
        itemAdapter = new ItemAdapter(this, filteredList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(itemAdapter);

        searchBar.addTextChangedListener(new TextWatcher() {
            // Implement TextWatcher methods
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {
                filterItems(query.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed
            }
        });

        logoutButton.setOnClickListener(view -> {
            mAuth.signOut(); // Sign out the user
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        });

        profileButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateItemList(); // Refresh data when activity resumes
    }

    private void populateItemList() {
        itemList.clear();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            String currentUserId = currentUser != null ? currentUser.getUid() : "";

            String selection = DatabaseHelper.COLUMN_STATUS + "=? AND " + DatabaseHelper.COLUMN_SELLER_ID + "!=?";
            String[] selectionArgs = {"active", currentUserId};

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
                    String itemSellerId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SELLER_ID));

                    if (description == null) description = "No description";
                    if (seller == null) seller = "Unknown Seller";

                    Item item = new Item(id, title, description, imageUri, price, seller, itemSellerId, country, postalCode, status);
                    itemList.add(item);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading items: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        filteredList.clear();
        filteredList.addAll(itemList);
        itemAdapter.notifyDataSetChanged();

        Log.d(TAG, "populateItemList: Item list updated. Total items: " + itemList.size());
    }

    private void filterItems(String searchText) {
        filteredList.clear();
        for (Item item : itemList) {
            if (item.getTitle().toLowerCase().contains(searchText)) {
                filteredList.add(item);
            }
        }
        itemAdapter.notifyDataSetChanged();
    }
}
