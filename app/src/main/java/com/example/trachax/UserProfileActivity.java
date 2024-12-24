package com.example.trachax;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class UserProfileActivity extends AppCompatActivity {

    private TextView fullNameTextView;
    private TextView contactNumberTextView;
    private TextView roleTextView;
    private TextView idNumberTextView;
    private TextView emailTextView;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        fullNameTextView = findViewById(R.id.show_fullname);
        contactNumberTextView = findViewById(R.id.show_contact);
        roleTextView = findViewById(R.id.show_role);
        idNumberTextView = findViewById(R.id.show_IdNumber);
        emailTextView = findViewById(R.id.show_email);

        // Initialize the DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Assuming the user's full name is passed as an extra
        String fullName = getIntent().getStringExtra("FULL_NAME");

        // Fetch user details based on full name
        User user = databaseHelper.getUserByFullName(fullName);

        if (user != null) {
            // Set the user's details to the TextViews
            fullNameTextView.setText(user.getFullName());
            contactNumberTextView.setText(user.getContactNumber());
            roleTextView.setText(user.getRole());
            idNumberTextView.setText(user.getIdNumber());
            emailTextView.setText(user.getEmail());
        } else {
            // Handle the case where the user is not found
            fullNameTextView.setText("User not found");
        }
    }
}
