package com.example.afinal.model;

import java.io.Serializable;

public class User implements Serializable {
    private long id;
    private String username;
    private String email;
    private String phone;
    private String userType;

    private String password; // Add this field if not already present




    public User(long id, String username, String email, String phone, String userType, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.userType = userType;
        this.password = password;
    }

    public User(long id, String username, String email, String phone, String userType) {
        this(id, username, email, phone, userType, ""); // Default empty password
    }

    // Getters and setters for all fields
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}