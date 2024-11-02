package com.example.management;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import com.example.management.model.Listing; // Import the Listing model

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

        // Populate the data into the template view using the data object
        textViewTitle.setText(listing.getTitle());
        textViewDescription.setText(listing.getDescription());
        textViewPrice.setText(String.format("$%.2f", listing.getPrice()));

        // Return the completed view to render on screen
        return convertView;
    }
}


