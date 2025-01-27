package com.example.trachax;

public class Driver {
    private String name;
    private String parentName;
    private String phoneNumber;

    // Constructor
    public Driver(String name, String parentName, String phoneNumber) {
        this.name = name;
        this.parentName = parentName;
        this.phoneNumber = phoneNumber;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}