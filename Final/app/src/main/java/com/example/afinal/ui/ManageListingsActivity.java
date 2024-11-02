package com.example.afinal.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.afinal.R;

public class ManageListingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_listings);

        // Find the button by its ID and set an OnClickListener
        Button addListingButton = findViewById(R.id.buttonAddListing);
        addListingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start AddEditListingActivity when the button is clicked
                Intent intent = new Intent(ManageListingsActivity.this, AddEditListingActivity.class);
                startActivity(intent);
            }
        });
    }
}
