package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.afinal.R;
import com.example.afinal.ui.AccountManagementActivity;
import com.example.afinal.ui.MarketManagementActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize buttons
        Button buttonAccountManagement = findViewById(R.id.buttonAccountManagement);
        Button buttonMarketManagement = findViewById(R.id.buttonMarketManagement);

        buttonAccountManagement.setOnClickListener(v -> {
            // Navigate to Account Management Activity
            Intent intent = new Intent(MainActivity.this, AccountManagementActivity.class);
            startActivity(intent);
        });

        buttonMarketManagement.setOnClickListener(v -> {
            // Navigate to Market Management Activity
            Intent intent = new Intent(MainActivity.this, MarketManagementActivity.class);
            startActivity(intent);
        });
    }
}
