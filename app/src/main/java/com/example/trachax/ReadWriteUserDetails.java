package com.example.trachax;

public class ReadWriteUserDetails {
    public String contactNumber;
    public String email;
    public String idNumber;
    public String fullname;
    public String role;

    // Default constructor (required for Firebase)
    public ReadWriteUserDetails() {
    }

    // Parameterized constructor for initialization
    public ReadWriteUserDetails(String contactNumber, String email, String idNumber, String fullname, String role) {
        this.contactNumber = contactNumber;
        this.email = email;
        this.idNumber = idNumber;
        this.fullname = fullname;
        this.role = role;
    }
}
