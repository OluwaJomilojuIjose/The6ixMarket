package com.example.afinal.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.afinal.R;
import com.example.afinal.model.Listing;

public class AddEditListingActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText editTextTitle, editTextDescription, editTextPrice;
    private ImageView imageProduct;
    private Uri imageUri;
    private Listing listing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_listing);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextPrice = findViewById(R.id.editTextPrice);
        imageProduct = findViewById(R.id.imageProduct);
        Button buttonSave = findViewById(R.id.buttonSave);
        Button buttonSelectImage = findViewById(R.id.buttonSelectImage);

        if (getIntent().hasExtra("listing")) {
            listing = (Listing) getIntent().getSerializableExtra("listing");
            populateFieldsForEdit();
        } else {
            listing = new Listing("", "", 0, "");

        }

        buttonSelectImage.setOnClickListener(v -> openImagePicker());
        buttonSave.setOnClickListener(v -> saveListing());
    }

    private void populateFieldsForEdit() {
        editTextTitle.setText(listing.getTitle());
        editTextDescription.setText(listing.getDescription());
        editTextPrice.setText(String.valueOf(listing.getPrice()));
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imageProduct.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveListing() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        double price;

        try {
            price = Double.parseDouble(editTextPrice.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid price", Toast.LENGTH_SHORT).show();
            return;
        }

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Title and description cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        listing.setTitle(title);
        listing.setDescription(description);
        listing.setPrice(price);
        Toast.makeText(this, "Listing saved successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
