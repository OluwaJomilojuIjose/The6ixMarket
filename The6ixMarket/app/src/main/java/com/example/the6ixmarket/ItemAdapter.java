package com.example.the6ixmarket;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private List<Item> itemList;
    private Context context;

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
        holder.imageView.setImageResource(item.getImageResId());

        // Set OnClickListener on the image view
        holder.imageView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ItemDetailActivity.class);
            intent.putExtra("ITEM_TITLE", item.getTitle());
            intent.putExtra("ITEM_DESCRIPTION", item.getDescription());
            intent.putExtra("ITEM_IMAGE", item.getImageResId());
            intent.putExtra("ITEM_PRICE", item.getPrice()); // Ensure you pass the price
            intent.putExtra("ITEM_SELLER", item.getSeller()); // Ensure you pass the seller
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView priceTextView;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.item_title);
            priceTextView = itemView.findViewById(R.id.item_price);
            imageView = itemView.findViewById(R.id.item_image);
        }
    }

    // In ItemAdapter.java
    public void updateItemList(List<Item> newList) {
        this.itemList = newList;
        notifyDataSetChanged();
    }


}
