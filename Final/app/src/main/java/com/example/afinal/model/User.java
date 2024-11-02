package com.example.afinal.model;

import java.io.Serializable;

public class User implements Serializable {
    private long id;
    private String username;
    private String email;
    private String phone;
    private String userType;

    public User(long id, String username, String email, String phone, String userType) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.userType = userType;
    }


    public long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getUserType() { return userType; }

    public void setId(long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setUserType(String userType) { this.userType = userType; }
}
