package com.example.the6ixmarket;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileDetailsActivity extends AppCompatActivity {

    private TextView nameTextView, emailTextView;
    private SQLiteDatabase database;
    private Button editProfileButton, backButton;

    private FirebaseAuth mAuth; // Firebase Authentication

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Initialize database and UI elements
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        database = dbHelper.getReadableDatabase();

        nameTextView = findViewById(R.id.name_text_view);
        emailTextView = findViewById(R.id.email_text_view);
        editProfileButton = findViewById(R.id.edit_profile_button);
        backButton = findViewById(R.id.back_button);

        // Load user details for the first time
        loadUserDetails();

        // Set up button click listener
        editProfileButton.setOnClickListener(v -> {
            // Navigate to EditProfileActivity
            Intent intent = new Intent(ProfileDetailsActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

        backButton.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload user details whenever the activity is resumed
        loadUserDetails();
    }

    private void loadUserDetails() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            Cursor cursor = database.query(DatabaseHelper.TABLE_USER,
                    new String[]{DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_EMAIL},
                    DatabaseHelper.COLUMN_UID + "=?",
                    new String[]{uid}, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
                String email = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL));

                nameTextView.setText(name);
                emailTextView.setText(email);

                cursor.close();
            } else {
                Toast.makeText(this, "Profile not found", Toast.LENGTH_SHORT).show();
                // Optionally navigate to EditProfileActivity to create a profile
            }
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity as there's no user logged in
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
