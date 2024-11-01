package com.example.management;

import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the button
        Button buttonAccountManagement = findViewById(R.id.buttonAccountManagement);
        Button buttonMarketManagement = findViewById(R.id.buttonMarketManagement);

        // Set an OnClickListener to handle button clicks
        buttonAccountManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to start the AccountManagementActivity
                Intent intent = new Intent(MainActivity.this, AccountManagementActivity.class);
                startActivity(intent); // Start the activity
            }
        });

//        buttonMarketManagement.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, MarketManagementActivity.class);
//                startActivity(intent); // Start the activity
//            }
//        });
    }
}