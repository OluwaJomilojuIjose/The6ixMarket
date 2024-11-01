package com.example.management;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.management.model.Listing;

public class AddEditListingActivity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextPrice;
    private Button buttonSave;

    private Listing listing; // Optional: Use this if editing an existing listing

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_listing);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextPrice = findViewById(R.id.editTextPrice);
        buttonSave = findViewById(R.id.buttonSave);

        // Check if we are editing an existing listing
        if (getIntent().hasExtra("listing")) {
            listing = (Listing) getIntent().getSerializableExtra("listing");
            if (listing != null) {
                editTextTitle.setText(listing.getTitle());
                editTextDescription.setText(listing.getDescription());
                editTextPrice.setText(String.valueOf(listing.getPrice()));
                buttonSave.setText("Update Listing"); // Change button text for update
            }
        } else {
            listing = new Listing("", "", 0); // New listing
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveListing();
            }
        });
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

        // Create or update listing logic
        if (listing != null) {
            listing = new Listing(title, description, price);
            Toast.makeText(this, "Listing updated!", Toast.LENGTH_SHORT).show();
            // You would also typically save this back to your database
        } else {
            listing = new Listing(title, description, price);
            Toast.makeText(this, "Listing added!", Toast.LENGTH_SHORT).show();
            // Save new listing to the database
        }

        // Finish the activity and go back to ManageListingsActivity
        finish();
    }
}

