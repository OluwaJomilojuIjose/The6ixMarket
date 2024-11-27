package com.example.the6ixmarket;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CreateListingActivity extends AppCompatActivity {

    private static final String TAG = "CreateListingActivity";

    private EditText titleEditText, priceEditText, descriptionEditText, countryEditText, postalCodeEditText;
    private ImageView listingImageView;
    private Button saveButton, backButton;
    private Uri selectedImageUri;
    private DatabaseHelper databaseHelper;
    private ActivityResultLauncher<String[]> galleryLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_listing);

        titleEditText = findViewById(R.id.title_edit_text);
        priceEditText = findViewById(R.id.price_edit_text);
        descriptionEditText = findViewById(R.id.description_edit_text);
        countryEditText = findViewById(R.id.country_edit_text);
        postalCodeEditText = findViewById(R.id.postal_code_edit_text);
        listingImageView = findViewById(R.id.listing_image_view);
        saveButton = findViewById(R.id.save_listing_button);
        backButton = findViewById(R.id.back_button);

        databaseHelper = new DatabaseHelper(this);

        FloatingActionButton galleryButton = findViewById(R.id.gallery_button);
        FloatingActionButton cameraButton = findViewById(R.id.camera_button);

        // Initialize gallery launcher with OpenDocument
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.OpenDocument(),
                uri -> {
                    if (uri != null) {
                        selectedImageUri = uri;
                        listingImageView.setImageURI(uri);

                        // Take persistable URI permissions here
                        try {
                            getContentResolver().takePersistableUriPermission(uri,
                                    Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            Log.d(TAG, "Persistable URI permission taken for: " + uri.toString());
                        } catch (SecurityException e) {
                            Log.e(TAG, "Failed to take persistable URI permission: " + e.getMessage());
                        }
                    }
                });

        // Initialize camera launcher
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bitmap photo = (Bitmap) result.getData().getExtras().get("data");
                        listingImageView.setImageBitmap(photo);
                        // Save bitmap to MediaStore and get URI
                        String savedImageURL = MediaStore.Images.Media.insertImage(
                                getContentResolver(), photo, "ListingImage", null);
                        selectedImageUri = Uri.parse(savedImageURL);
                        Log.d(TAG, "Image captured and saved: " + selectedImageUri.toString());
                    }
                });

        // Set up gallery button click listener
        galleryButton.setOnClickListener(view -> {
            galleryLauncher.launch(new String[]{"image/*"});
        });

        // Set up camera button click listener
        cameraButton.setOnClickListener(view -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraLauncher.launch(intent);
        });

        saveButton.setOnClickListener(view -> {
            String title = titleEditText.getText().toString().trim();
            String price = priceEditText.getText().toString().trim();
            String description = descriptionEditText.getText().toString().trim();
            String country = countryEditText.getText().toString().trim();
            String postalCode = postalCodeEditText.getText().toString().trim();

            if (title.isEmpty() || price.isEmpty() || selectedImageUri == null || country.isEmpty() || postalCode.isEmpty()) {
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            } else {
                saveListing(title, price, selectedImageUri.toString(), description, country, postalCode);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveListing(String title, String price, String imageUri, String description, String country, String postalCode) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, title);
        values.put(DatabaseHelper.COLUMN_PRICE, price);
        values.put(DatabaseHelper.COLUMN_IMAGE_URI, imageUri);
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, description);
        values.put(DatabaseHelper.COLUMN_SELLER, "Seller Name"); // Replace with actual seller info
        values.put(DatabaseHelper.COLUMN_COUNTRY, country);
        values.put(DatabaseHelper.COLUMN_POSTAL_CODE, postalCode);
        values.put(DatabaseHelper.COLUMN_STATUS, "active"); // Set status to active

        long newRowId = db.insert(DatabaseHelper.TABLE_LISTINGS, null, values);
        if (newRowId != -1) {
            Toast.makeText(this, "Listing saved! ID: " + newRowId, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Listing saved with ID: " + newRowId);
            finish(); // Close activity
        } else {
            Toast.makeText(this, "Failed to save listing!", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Failed to save listing to database.");
        }
    }
}
