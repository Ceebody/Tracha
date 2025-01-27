package com.example.trachax;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {
    public static final String ROLE_ADMIN = "Admin";
    public static final String ROLE_PARENT = "Parent";
    public static final String ROLE_DRIVER = "Driver";

    private String id;
    private String name; // Name of the user
    private String email;
    private String phone;
    private String role;
    private String address;
    private String idNumber;
    private String profileImageUrl; // Added field for profile image URL
    private String status; // Added field for user status (e.g., "online" or "offline")

    // Default constructor required for Firebase
    public User() {
    }

    // Full constructor
    public User(String id, String name, String email, String phone, String role, String address, String profileImageUrl, String idNumber, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.address = address;
        this.idNumber = idNumber;
        this.profileImageUrl = profileImageUrl; // Initialize the profile image URL
        this.status = status; // Initialize the status
    }

    // Getters with null safety
    public String getId() {
        return id != null ? id : "Unknown ID";
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name != null ? name : "Unknown Name";
    }

    public String getIdNumber() {
        return idNumber != null ? idNumber : "Unknown IdNumber";
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter for profile image URL
    public String getProfileImageUrl() {
        return profileImageUrl != null ? profileImageUrl : ""; // Returns empty string if profileImageUrl is null
    }

    // Setter for profile image URL
    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getEmail() {
        return email != null ? email : "No Email Provided";
    }

    public void setEmail(String email) {
        if (email != null && email.contains("@")) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    public String getPhone() {
        return phone != null ? phone : "No Phone Number Provided";
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role != null ? role : "Unknown Role";
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAddress() {
        return address != null ? address : "No Address Provided";
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Getter and Setter for status
    public String getStatus() {
        return status != null ? status : "offline"; // Default to "offline" if null
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Convert to Map for Firebase
    public Map<String, Object> toMap() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", id);
        userMap.put("name", name);
        userMap.put("email", email);
        userMap.put("phone", phone);
        userMap.put("role", role);
        userMap.put("address", address);
        userMap.put("profileImageUrl", profileImageUrl); // Added profile image URL to map
        userMap.put("status", status); // Added status to map
        return userMap;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", role='" + role + '\'' +
                ", address='" + address + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", status='" + status + '\'' + // Added status to string representation
                '}';
    }
}
