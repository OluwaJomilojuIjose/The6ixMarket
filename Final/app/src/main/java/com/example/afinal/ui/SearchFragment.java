package com.example.afinal.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.afinal.R;
import com.example.afinal.database.ProductRepository;
import com.example.afinal.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchFragment extends Fragment {

    private EditText searchInput;
    private Spinner sortSpinner;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private ProductRepository productRepository;
    private List<Product> productList;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Initialize views
        searchInput = view.findViewById(R.id.search_input);
        sortSpinner = view.findViewById(R.id.sort_spinner);
        recyclerView = view.findViewById(R.id.recycler_view);

        // Initialize repository and data
        productRepository = new ProductRepository(requireContext());
        productList = productRepository.getAllProducts();

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        productAdapter = new ProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);

        setupSearchInputListener();
        setupSortSpinnerListener();

        return view;
    }

    private void setupSearchInputListener() {
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProductList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    private void setupSortSpinnerListener() {
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortProductList(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void filterProductList(String query) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(product);
            }
        }
        productAdapter.updateList(filteredList);
    }

    private void sortProductList(int position) {
        switch (position) {
            case 0: // Sort by price (low to high)
                Collections.sort(productList, Comparator.comparingDouble(Product::getPrice));
                break;
            case 1: // Sort by price (high to low)
                Collections.sort(productList, (p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));
                break;
            default:
                break;
        }
        productAdapter.updateList(productList);
    }
}
