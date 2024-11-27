package com.example.the6ixmarket;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize FirebaseAuth and check user authentication
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        logoutButton = findViewById(R.id.logout);
        profileButton = findViewById(R.id.profile_button);
        userDetails = findViewById(R.id.user_details);
        recyclerView = findViewById(R.id.recycler_view);
        searchBar = findViewById(R.id.search_bar);

        // If user is not logged in, redirect to Login activity
        if (user == null) {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        } else {
            userDetails.setText(user.getEmail()); // Show user's email
        }

        databaseHelper = new DatabaseHelper(this);

        itemList = new ArrayList<>();
        filteredList = new ArrayList<>();
        itemAdapter = new ItemAdapter(this, filteredList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(itemAdapter);

        // Set up search functionality
        searchBar.addTextChangedListener(new TextWatcher() {
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

        // Handle logout button click
        logoutButton.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut(); // Sign out from Firebase
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        });

        // Handle profile button click
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
            cursor = db.query(DatabaseHelper.TABLE_LISTINGS, null, null, null, null, null, null);

            if (cursor != null) {
                Log.d(TAG, "Cursor count: " + cursor.getCount());
                while (cursor.moveToNext()) {
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
                    String price = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRICE));
                    String imageUri = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMAGE_URI));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION));
                    String seller = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SELLER));
                    String country = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COUNTRY)); // New
                    String postalCode = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_POSTAL_CODE)); // New

                    if (description == null) description = "No description";
                    if (seller == null) seller = "Unknown Seller";

                    Item item = new Item(title, description, imageUri, price, seller, country, postalCode);
                    itemList.add(item);

                    Log.d(TAG, "Loaded item: " + title);
                }
            } else {
                Log.d(TAG, "Cursor is null");
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