package com.example.afinal.model;

import java.io.Serializable;

public class Product implements Serializable {
    private long id;
    private String name;
    private double price;
    private String condition;
    private String description;
    private double latitude;
    private double longitude;
    private String imageUri;
    private long userId;

    public Product(long id, String name, double price, String condition, String description, double latitude, double longitude, String imageUri, long userId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.condition = condition;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageUri = imageUri;
        this.userId = userId;
    }

    public Product(String name, double price, String condition, String description, double latitude, double longitude, String imageUri, long userId) {
        this.name = name;
        this.price = price;
        this.condition = condition;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageUri = imageUri;
        this.userId = userId;
    }


    public long getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getCondition() { return condition; }
    public String getDescription() { return description; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getImageUri() { return imageUri; }
    public long getUserId() { return userId; }

    public void setId(long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setCondition(String condition) { this.condition = condition; }
    public void setDescription(String description) { this.description = description; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public void setImageUri(String imageUri) { this.imageUri = imageUri; }
    public void setUserId(long userId) { this.userId = userId; }
}
