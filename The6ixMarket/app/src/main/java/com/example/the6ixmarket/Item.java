
package com.example.the6ixmarket;

public class Item {
    private String title;
    private String description;
    private int imageResId;
    private String price; // Add price attribute
    private String seller; // Seller attribute

    public Item(String title, String description, int imageResId, String price, String seller) {
        this.title = title;
        this.description = description;
        this.imageResId = imageResId;
        this.price = price; // Initialize price
        this.seller = seller; // Initialize seller
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getPrice() { // Getter for price
        return price;
    }

    public String getSeller() { // Getter for seller
        return seller;
    }
}
