package com.example.afinal.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.afinal.R;
import com.example.afinal.model.Listing;

import java.util.List;

public class ListingAdapter extends ArrayAdapter<Listing> {
    private final Context context;
    private final List<Listing> listings;

    public ListingAdapter(Context context, List<Listing> listings) {
        super(context, 0, listings);
        this.context = context;
        this.listings = listings;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Listing listing = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listing_item, parent, false);
        }

        // Lookup view for data population
        TextView textViewTitle = convertView.findViewById(R.id.textViewTitle);
        TextView textViewDescription = convertView.findViewById(R.id.textViewDescription);
        TextView textViewPrice = convertView.findViewById(R.id.textViewPrice);
        ImageView productImage = convertView.findViewById(R.id.product_image);

        // Populate the data into the template view using the data object
        textViewTitle.setText(listing.getTitle());
        textViewDescription.setText(listing.getDescription());
        textViewPrice.setText(String.format("$%.2f", listing.getPrice()));


        Button contactSellerButton = convertView.findViewById(R.id.contact_seller_button);
        contactSellerButton.setOnClickListener(v -> {

        });

        return convertView;
    }
}
