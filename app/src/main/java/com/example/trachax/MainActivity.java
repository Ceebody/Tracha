package com.example.trachax;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check shared preferences for user role
        SharedPreferences sharedPreferences = getSharedPreferences("TrachaxPrefs", MODE_PRIVATE);
        String userRole = sharedPreferences.getString("user_role", null);

        if (userRole == null) {
            // No role found, display login/register page
            setContentView(R.layout.activity_main);
            initializeUI();
        } else {
            // Redirect based on the user role
            redirectToRoleSpecificActivity(userRole);
        }
    }

    private void initializeUI() {
        // Initialize buttons
        loginButton = findViewById(R.id.button_login);
        registerButton = findViewById(R.id.register_button);

        // Set click listeners
        loginButton.setOnClickListener(v -> openLoginRoles());
        registerButton.setOnClickListener(v -> openRegisterActivity());
    }

    private void openLoginRoles() {
        // Navigate to LoginRoles activity
        Intent intent = new Intent(MainActivity.this, LoginRoles.class);
        startActivity(intent);
    }

    private void openRegisterActivity() {
        // Navigate to RegisterActivity
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private void redirectToRoleSpecificActivity(String userRole) {
        Intent intent;
        if (userRole.equals("parent")) {
            // Redirect to Parent Dashboard
            intent = new Intent(this, ParentDashboard.class);
        } else if (userRole.equals("driver")) {
            // Redirect to Driver Home
            intent = new Intent(this, DriverHomeActivity.class);
        } else {
            // Default case (optional)
            intent = new Intent(this, LoginRoles.class);
        }
        startActivity(intent);
        finish(); // Close MainActivity to prevent going back
    }
}
