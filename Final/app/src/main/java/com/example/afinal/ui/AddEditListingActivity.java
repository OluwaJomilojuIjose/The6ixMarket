package com.example.afinal.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.afinal.R;
import com.example.afinal.model.Listing;

public class AddEditListingActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 100;

    private EditText editTextTitle, editTextDescription, editTextPrice;
    private Button buttonSave, buttonSelectImage, buttonBack;
    private ImageView imageProduct;
    private Listing listing;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_listing);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextPrice = findViewById(R.id.editTextPrice);
        buttonSave = findViewById(R.id.buttonSave);
        buttonSelectImage = findViewById(R.id.buttonSelectImage);
        imageProduct = findViewById(R.id.imageProduct);
        buttonBack = findViewById(R.id.backButton);

        buttonBack.setOnClickListener(v -> finish());

        if (getIntent().hasExtra("listing")) {
            listing = (Listing) getIntent().getSerializableExtra("listing");
            if (listing != null) {
                editTextTitle.setText(listing.getTitle());
                editTextDescription.setText(listing.getDescription());
                editTextPrice.setText(String.valueOf(listing.getPrice()));
                buttonSave.setText("Update Listing");
            }
        } else {
            listing = new Listing("", "", 0);
        }

        buttonSelectImage.setOnClickListener(v -> openImagePicker());
        buttonSave.setOnClickListener(v -> saveListing());
    }

    private void openImagePicker() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(this, "Permission denied to access storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageProduct.setImageURI(imageUri);
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

        if (listing != null) {
            listing.setTitle(title);
            listing.setDescription(description);
            listing.setPrice(price);
            Toast.makeText(this, "Listing updated!", Toast.LENGTH_SHORT).show();
        } else {
            listing = new Listing(title, description, price);
            Toast.makeText(this, "Listing added!", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}
