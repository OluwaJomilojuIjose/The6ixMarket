package com.example.the6ixmarket;

public class Item {
    private String title;
    private String description;
    private String imageUri;
    private String price;
    private String seller;
    private String country; // New field
    private String postalCode; // New field

    public Item(String title, String description, String imageUri, String price, String seller, String country, String postalCode) {
        this.title = title;
        this.description = description;
        this.imageUri = imageUri;
        this.price = price;
        this.seller = seller;
        this.country = country;
        this.postalCode = postalCode;
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getImageUri() { return imageUri; }
    public String getPrice() { return price; }
    public String getSeller() { return seller; }
    public String getCountry() { return country; } // New getter
    public String getPostalCode() { return postalCode; } // New getter

    // Setters (if needed)
    // public void setTitle(String title) { this.title = title; }
    // Add setters if you plan to modify item data
}
