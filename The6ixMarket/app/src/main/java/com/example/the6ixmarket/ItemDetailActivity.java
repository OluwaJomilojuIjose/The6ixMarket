//package com.example.the6ixmarket;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.util.ArrayList;
//
//public class ItemDetailActivity extends AppCompatActivity {
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_item_detail);
//
//        ImageView itemImageView = findViewById(R.id.item_detail_image);
//        TextView itemTitleTextView = findViewById(R.id.item_detail_title);
//        TextView itemPriceTextView = findViewById(R.id.item_detail_price);
//        TextView itemDescriptionTextView = findViewById(R.id.item_detail_description);
//        TextView itemSellerTextView = findViewById(R.id.item_detail_seller);
//
//        // Get data from intent
//        Intent intent = getIntent();
//        String title = intent.getStringExtra("ITEM_TITLE");
//        String description = intent.getStringExtra("ITEM_DESCRIPTION");
//        int imageResId = intent.getIntExtra("ITEM_IMAGE", 0);
//        String price = intent.getStringExtra("ITEM_PRICE");
//        String seller = intent.getStringExtra("ITEM_SELLER");
//
//        // Set data to views
//        itemTitleTextView.setText(title);
//        itemDescriptionTextView.setText(description);
//        itemImageView.setImageResource(imageResId);
//        itemPriceTextView.setText(price);
//        itemSellerTextView.setText(seller);
//
//    }
//
//}


package com.example.the6ixmarket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ItemDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        ImageView itemImageView = findViewById(R.id.item_detail_image);
        TextView itemTitleTextView = findViewById(R.id.item_detail_title);
        TextView itemPriceTextView = findViewById(R.id.item_detail_price);
        TextView itemDescriptionTextView = findViewById(R.id.item_detail_description);
        TextView itemSellerTextView = findViewById(R.id.item_detail_seller);
        Button messageSellerButton = findViewById(R.id.message_seller_button); // Add to cart button

        // Get data from intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("ITEM_TITLE");
        String description = intent.getStringExtra("ITEM_DESCRIPTION");
        int imageResId = intent.getIntExtra("ITEM_IMAGE", 0);
        String price = intent.getStringExtra("ITEM_PRICE");
        String seller = intent.getStringExtra("ITEM_SELLER");

        // Set data to views
        itemTitleTextView.setText(title);
        itemDescriptionTextView.setText(description);
        itemImageView.setImageResource(imageResId);
        itemPriceTextView.setText(price);
        itemSellerTextView.setText(seller);

        // Set up the button click listener
        messageSellerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to MainActivity
                Intent intent = new Intent(ItemDetailActivity.this, MessagingActivity.class);
                intent.putExtra("ITEM_TITLE", title);
                intent.putExtra("SELLER_NAME", seller);
                startActivity(intent);
            }
        });
    }
}
