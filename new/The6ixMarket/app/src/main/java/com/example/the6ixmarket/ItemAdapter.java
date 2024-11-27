package com.example.the6ixmarket;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log; // Import Log
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private final Context context;
    private final List<Item> itemList;

    public ItemAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.titleTextView.setText(item.getTitle());
        holder.priceTextView.setText(item.getPrice());
        holder.countryTextView.setText(item.getCountry()); // New
        holder.postalCodeTextView.setText(item.getPostalCode()); // New

        Log.d("ItemAdapter", "Binding item at position " + position + ": " + item.getTitle());

        try {
            if (item.getImageUri() != null && !item.getImageUri().isEmpty()) {
                holder.imageView.setImageURI(Uri.parse(item.getImageUri()));
            } else {
                holder.imageView.setImageResource(R.drawable.placeholder_image);
            }
        } catch (Exception e) {
            holder.imageView.setImageResource(R.drawable.placeholder_image);
            Log.e("ItemAdapter", "Error setting image URI: " + e.getMessage());
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ItemDetailActivity.class);
            intent.putExtra("ITEM_TITLE", item.getTitle());
            intent.putExtra("ITEM_DESCRIPTION", item.getDescription());
            intent.putExtra("ITEM_IMAGE", item.getImageUri());
            intent.putExtra("ITEM_PRICE", item.getPrice());
            intent.putExtra("ITEM_SELLER", item.getSeller());
            intent.putExtra("ITEM_COUNTRY", item.getCountry()); // New
            intent.putExtra("ITEM_POSTAL_CODE", item.getPostalCode()); // New
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView, priceTextView, countryTextView, postalCodeTextView;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.item_title);
            priceTextView = itemView.findViewById(R.id.item_price);
            countryTextView = itemView.findViewById(R.id.item_country); // New
            postalCodeTextView = itemView.findViewById(R.id.item_postal_code); // New
            imageView = itemView.findViewById(R.id.item_image);
        }
    }
}
