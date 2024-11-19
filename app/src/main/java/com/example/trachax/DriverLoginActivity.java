package com.example.trachax;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.trachax.DriverDashboardActivity;

import androidx.appcompat.app.AppCompatActivity;

public class DriverLoginActivity extends AppCompatActivity {

    private EditText carDriver, emailDriver, passwordDriver;
    private Button loginButton;
    private ProgressBar progressBar;
    private TextView signUpText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_driver);

        // Initialize views
        carDriver = findViewById(R.id.car_driver);
        emailDriver = findViewById(R.id.email_driver);
        passwordDriver = findViewById(R.id.password_driver);
        loginButton = findViewById(R.id.driver_login_btn);
        progressBar = findViewById(R.id.progress_bar_driver);
        signUpText = findViewById(R.id.signup_driver);

        // Set login button click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginDriver();
            }
        });

        // Set sign-up text click listener
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Driver Registration Activity
                Intent intent = new Intent(DriverLoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginDriver() {
        String carNumber = carDriver.getText().toString().trim();
        String email = emailDriver.getText().toString().trim();
        String password = passwordDriver.getText().toString().trim();

        // Validate inputs
        if (carNumber.isEmpty()) {
            carDriver.setError("Car number is required");
            carDriver.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            emailDriver.setError("Email is required");
            emailDriver.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            passwordDriver.setError("Password is required");
            passwordDriver.requestFocus();
            return;
        }

        // Simulate login process with ProgressBar
        progressBar.setVisibility(View.VISIBLE);

        // For now, simulate a successful login. Replace this with your authentication logic.
        loginButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);

                // Mock success case
                if (email.equals("driver@example.com") && password.equals("driver123")) {
                    Toast.makeText(DriverLoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                    // Navigate to DriverDashboardActivity
                    Intent intent = new Intent(DriverLoginActivity.this, DriverDashboardActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(DriverLoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        }, 2000); // Simulate network delay
    }
}
