package com.example.trachax;

public class DriverModel {
    private String driverName;
    private String driverPhone;
    private String driverLicense;

    // Constructor with three String parameters
    public DriverModel(String driverName, String driverPhone, String driverLicense) {
        this.driverName = driverName;
        this.driverPhone = driverPhone;
        this.driverLicense = driverLicense;
    }

    // Getters and setters for each field (optional)
    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getDriverLicense() {
        return driverLicense;
    }

    public void setDriverLicense(String driverLicense) {
        this.driverLicense = driverLicense;
    }
}
