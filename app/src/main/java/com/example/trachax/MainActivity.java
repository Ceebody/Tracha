package com.example.trachax;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private Button registerButton;
    private ImageView logo;

    private int tapCount = 0; // Variable to count taps
    private static final int MAX_TAPS = 7;
    private long firstTapTime = 0; // To track time between taps

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check shared preferences for user role
        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        String userRole = sharedPreferences.getString("user_role", null);

        if (TextUtils.isEmpty(userRole)) {
            // No role found, display login/register page
            setContentView(R.layout.activity_main);
            initializeUI();
        } else {
            // Redirect based on the user role
            redirectToRoleSpecificActivity(userRole);
        }
    }

    private void initializeUI() {
        // Initialize buttons and logo
        loginButton = findViewById(R.id.button_login);
        registerButton = findViewById(R.id.register_button);
        logo = findViewById(R.id.logo);

        // Set click listeners for login and register buttons
        loginButton.setOnClickListener(v -> openLoginRoles());
        registerButton.setOnClickListener(v -> openRegisterActivity());

        // Set tap listener for the logo to trigger special actions
        logo.setOnClickListener(v -> handleLogoTaps());
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
        switch (userRole) {
            case "parent":
                // Redirect to Parent Dashboard
                intent = new Intent(this, ParentDashboard.class);
                break;
            case "driver":
                // Redirect to Driver Home
                intent = new Intent(this, DriverHomeActivity.class);
                break;
            default:
                // Handle unknown role gracefully
                intent = new Intent(this, LoginRoles.class);
                break;
        }
        startActivity(intent);
        finish(); // Finish current activity
    }

    private void handleLogoTaps() {
        long currentTime = System.currentTimeMillis();

        // Reset tap count if the time between taps exceeds 2 seconds
        if (firstTapTime == 0 || (currentTime - firstTapTime > 2000)) {
            tapCount = 0;
            firstTapTime = currentTime;
        }

        tapCount++;

        if (tapCount == MAX_TAPS) {
            // Trigger the bus driver login activity after 7 taps
            openBusDriverLoginActivity();
            tapCount = 0; // Reset the tap count
        }
    }

    private void openBusDriverLoginActivity() {
        // Redirect to BusDriverLoginActivity
        Intent intent = new Intent(MainActivity.this, BusDriverLoginActivity.class);
        startActivity(intent);
    }
}
