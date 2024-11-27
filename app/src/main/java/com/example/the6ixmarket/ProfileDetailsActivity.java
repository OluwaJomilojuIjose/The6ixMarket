package com.example.the6ixmarket;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileDetailsActivity extends AppCompatActivity {

    private TextView nameTextView, emailTextView;
    private SQLiteDatabase database;
    private Button editProfileButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        // Initialize database and UI elements
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        database = dbHelper.getReadableDatabase();

        nameTextView = findViewById(R.id.name_text_view);
        emailTextView = findViewById(R.id.email_text_view);
        editProfileButton = findViewById(R.id.edit_profile_button); // Initialize button
        backButton = findViewById(R.id.back_button);

        // Load user details for the first time
        loadUserDetails();

        // Set up button click listener
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to EditProfileActivity
                Intent intent = new Intent(ProfileDetailsActivity.this, EditProfileActivity.class);
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

    @Override
    protected void onResume() {
        super.onResume();
        // Reload user details whenever the activity is resumed
        loadUserDetails();
    }

    private void loadUserDetails() {
        Cursor cursor = database.query(DatabaseHelper.TABLE_USER,
                new String[]{DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_EMAIL},
                null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
            String email = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL));

            nameTextView.setText(name);
            emailTextView.setText(email);

            cursor.close();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (database != null && database.isOpen()) {
            database.close();
        }
    }
}
