package com.example.trachax;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class DriverLoginActivity extends AppCompatActivity {

    private EditText emailDriver, passwordDriver;
    private Button loginButton;
    private ProgressBar progressBar;
    private TextView signUpText, forgotPasswordText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_driver);

        // Initialize views
        emailDriver = findViewById(R.id.email_driver);
        passwordDriver = findViewById(R.id.password_driver);
        loginButton = findViewById(R.id.driver_login_btn);
        progressBar = findViewById(R.id.progress_bar_driver);
        signUpText = findViewById(R.id.signup_driver);
        forgotPasswordText = findViewById(R.id.forgot_password_driver);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Set login button click listener
        loginButton.setOnClickListener(v -> loginDriver());

        // Set sign-up text click listener
        signUpText.setOnClickListener(v -> {
            // Navigate to Driver Registration Activity
            Intent intent = new Intent(DriverLoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Set forgot password text click listener
        forgotPasswordText.setOnClickListener(v -> {
            Intent intent = new Intent(DriverLoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

    private void loginDriver() {
        String email = emailDriver.getText().toString().trim();
        String password = passwordDriver.getText().toString().trim();

        // Validate inputs
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

        // Show progress bar while authenticating
        progressBar.setVisibility(View.VISIBLE);

        // Firebase Authentication to log in the driver
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(DriverLoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        // Navigate to DriverDashboardActivity
                        Intent intent = new Intent(DriverLoginActivity.this, DriverHomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(DriverLoginActivity.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
