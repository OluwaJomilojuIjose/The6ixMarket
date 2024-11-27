
package com.example.the6ixmarket;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;
    RecyclerView recyclerView;
    ItemAdapter itemAdapter;
    List<Item> itemList;
    List<Item> filteredList; // List for filtered items
    EditText searchBar; // Search bar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        textView = findViewById(R.id.user_details);
        searchBar = findViewById(R.id.search_bar); // Initialize the search bar
        user = auth.getCurrentUser();

        // Check if user is logged in
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            textView.setText(user.getEmail());
        }

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemList = new ArrayList<>();
        filteredList = new ArrayList<>(); // Initialize the filtered list

        // Populate the itemList
        populateItemList();

        itemAdapter = new ItemAdapter(this, itemList);
        recyclerView.setAdapter(itemAdapter);

        // Search bar functionality
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchText = charSequence.toString().toLowerCase();
                filterItems(searchText); // Call the filter function
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void populateItemList() {
        // Add items to the list
        itemList.add(new Item("Item 1", "Description for item 1", R.drawable.item1_image, "$1,000", "Seller 1"));
        itemList.add(new Item("Item 2", "Description for item 2", R.drawable.item2_image, "$400", "Seller 2"));
        itemList.add(new Item("Item 3", "Description for item 3", R.drawable.item3_image, "$15,000", "Seller 3"));
        itemList.add(new Item("Item 4", "Description for item 4", R.drawable.item4_image, "$125", "Seller 4"));
        itemList.add(new Item("Item 5", "Description for item 5", R.drawable.item5_image, "$450", "Seller 5"));
        itemList.add(new Item("Item 6", "Description for item 6", R.drawable.item6_image, "$195", "Seller 6"));
        itemList.add(new Item("Item 7", "Description for item 7", R.drawable.item7_image, "$195", "Seller 7"));
        // Add more items as needed
        filteredList.addAll(itemList); // Initialize the filtered list with all items
    }

    private void filterItems(String searchText) {
        filteredList.clear();
        for (Item item : itemList) {
            if (item.getTitle().toLowerCase().contains(searchText)) {
                filteredList.add(item);
            }
        }
        itemAdapter.updateItemList(filteredList); // Update the adapter with filtered items
    }
}
