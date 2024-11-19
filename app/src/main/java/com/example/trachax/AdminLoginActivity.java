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

import com.example.trachax.AdminDashboardActivity;
import com.example.trachax.R;
import com.example.trachax.RegisterActivity;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText emailAdmin, passwordAdmin;
    private Button loginButton;
    private ProgressBar progressBar;
    private TextView signUpText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        // Initialize views
        emailAdmin = findViewById(R.id.login_admin);
        passwordAdmin = findViewById(R.id.password_admin);
        loginButton = findViewById(R.id.Admin_login_btn);
        progressBar = findViewById(R.id.progress_bar_admin);
        signUpText = findViewById(R.id.signup_admin);

        // Set login button click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAdmin();
            }
        });

        // Set sign-up text click listener
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Admin Registration Activity
                Intent intent = new Intent(AdminLoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginAdmin() {
        String email = emailAdmin.getText().toString().trim();
        String password = passwordAdmin.getText().toString().trim();

        // Validate inputs
        if (email.isEmpty()) {
            emailAdmin.setError("Email is required");
            emailAdmin.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            passwordAdmin.setError("Password is required");
            passwordAdmin.requestFocus();
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
                if (email.equals("admin@example.com") && password.equals("admin123")) {
                    Toast.makeText(AdminLoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                    // Navigate to AdminDashboardActivity
                    Intent intent = new Intent(AdminLoginActivity.this, AdminDashboardActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(AdminLoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        }, 2000); // Simulate network delay
    }
}
