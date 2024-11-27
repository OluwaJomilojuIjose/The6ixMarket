package com.example.the6ixmarket;

public class Item {
    private String id; // New field
    private String title;
    private String description;
    private String imageUri;
    private String price;
    private String seller;
    private String country;
    private String postalCode;
    private String status; // New field

    public Item(String id, String title, String description, String imageUri, String price, String seller, String country, String postalCode, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUri = imageUri;
        this.price = price;
        this.seller = seller;
        this.country = country;
        this.postalCode = postalCode;
        this.status = status;
    }

    // Getters
    public String getId() { return id; } // New getter
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getImageUri() { return imageUri; }
    public String getPrice() { return price; }
    public String getSeller() { return seller; }
    public String getCountry() { return country; }
    public String getPostalCode() { return postalCode; }
    public String getStatus() { return status; } // New getter

    // Setters (if needed)
    // public void setStatus(String status) { this.status = status; }
}
