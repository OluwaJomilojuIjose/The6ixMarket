package com.example.the6ixmarket;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editName, editEmail;
    private Button saveButton, backButton;
    private SQLiteDatabase database;

    private FirebaseAuth mAuth; // Firebase Authentication

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Initialize database helper
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        database = dbHelper.getWritableDatabase();

        // Initialize UI elements
        editName = findViewById(R.id.edit_name);
        editEmail = findViewById(R.id.edit_email);
        saveButton = findViewById(R.id.save_button);
        backButton = findViewById(R.id.back_button);

        // Load existing user details
        loadUserDetails();

        // Set click listener for Save button
        saveButton.setOnClickListener(v -> saveUserDetails());

        backButton.setOnClickListener(v -> finish());
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

                editName.setText(name);
                editEmail.setText(email);

                cursor.close();
            } else {
                // No existing profile, you can choose to pre-fill email from FirebaseUser
                editEmail.setText(currentUser.getEmail());
            }
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity as there's no user logged in
        }
    }

    private void saveUserDetails() {
        String name = editName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Both fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_NAME, name);
            values.put(DatabaseHelper.COLUMN_EMAIL, email);
            values.put(DatabaseHelper.COLUMN_UID, uid);

            int rowsUpdated = database.update(DatabaseHelper.TABLE_USER, values,
                    DatabaseHelper.COLUMN_UID + "=?", new String[]{uid});

            if (rowsUpdated == 0) {
                // Insert new profile if it doesn't exist
                database.insert(DatabaseHelper.TABLE_USER, null, values);
            }

            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
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
