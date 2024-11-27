package com.example.afinal.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.afinal.R;
import com.example.afinal.database.ProductRepository;
import com.example.afinal.model.Product;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class AddProductFragment extends Fragment implements OnMapReadyCallback {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "AddProductFragment";

    private EditText productName, productCondition, productDescription, productPrice;
    private ImageView productImage;
    private Uri imageUri;
    private ProductRepository productRepository;

    private double selectedLatitude = 43.65107; // Default: Toronto, ON
    private double selectedLongitude = -79.347015;

    public AddProductFragment() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);

        // Initialize repository
        try {
            productRepository = new ProductRepository(requireContext());
        } catch (Exception e) {
            Log.e(TAG, "Failed to initialize ProductRepository", e);
            Toast.makeText(getContext(), "Database initialization failed. Please restart the app.", Toast.LENGTH_SHORT).show();
            return view;
        }

        // Initialize views
        productName = view.findViewById(R.id.edit_product_name);
        productCondition = view.findViewById(R.id.edit_product_condition);
        productDescription = view.findViewById(R.id.edit_product_description);
        productPrice = view.findViewById(R.id.edit_product_price);
        productImage = view.findViewById(R.id.image_product);
        Button selectImageButton = view.findViewById(R.id.button_select_image);
        Button addProductButton = view.findViewById(R.id.button_add_product);

        // Map setup
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Log.e(TAG, "Map fragment is null");
        }

        // Button listeners
        selectImageButton.setOnClickListener(v -> openImagePicker());
        addProductButton.setOnClickListener(v -> {
            if (validateInputs()) {
                addProductToDatabase(view);
            }
        });

        return view;
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null) {
            imageUri = data.getData();
            loadImagePreview();
        } else {
            Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadImagePreview() {
        Glide.with(this)
                .load(imageUri)
                .placeholder(R.drawable.ic_placeholder_image)
                .error(R.drawable.ic_error_image)
                .into(productImage);
    }

    private boolean validateInputs() {
        String name = productName.getText().toString().trim();
        String condition = productCondition.getText().toString().trim();
        String description = productDescription.getText().toString().trim();
        String priceStr = productPrice.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(condition) || TextUtils.isEmpty(description) ||
                TextUtils.isEmpty(priceStr) || imageUri == null) {
            Toast.makeText(getContext(), "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            Double.parseDouble(priceStr); // Validate price format
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid price value", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void addProductToDatabase(View view) {
        String name = productName.getText().toString().trim();
        String condition = productCondition.getText().toString().trim();
        String description = productDescription.getText().toString().trim();
        double price = Double.parseDouble(productPrice.getText().toString().trim());
        long userId = 1; // Mock user ID; replace with actual logged-in user ID

        long result = productRepository.insertProduct(new Product(name, price, condition, description, selectedLatitude, selectedLongitude, imageUri.toString(), userId));

        if (result != -1) {
            Toast.makeText(getContext(), "Product added successfully", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).navigate(R.id.action_addProductFragment_to_homeFragment);
        } else {
            Toast.makeText(getContext(), "Failed to add product. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setOnMapClickListener(latLng -> {
            selectedLatitude = latLng.latitude;
            selectedLongitude = latLng.longitude;
            Toast.makeText(getContext(), "Location selected: " + selectedLatitude + ", " + selectedLongitude, Toast.LENGTH_SHORT).show();
        });
    }
}
