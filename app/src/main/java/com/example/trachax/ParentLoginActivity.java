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

public class ParentLoginActivity extends AppCompatActivity {

    private EditText emailParent, passwordParent;
    private Button loginButton;
    private ProgressBar progressBar;
    private TextView signUpText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_parent);

        // Initialize views
        emailParent = findViewById(R.id.email_parent);
        passwordParent = findViewById(R.id.password_parent);
        loginButton = findViewById(R.id.parent_login_btn);
        progressBar = findViewById(R.id.progress_bar_parent);
        signUpText = findViewById(R.id.signup_parent);

        // Set login button click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginParent();
            }
        });

        // Set sign-up text click listener
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Parent Registration Activity
                Intent intent = new Intent(ParentLoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginParent() {
        String email = emailParent.getText().toString().trim();
        String password = passwordParent.getText().toString().trim();

        // Validate inputs
        if (email.isEmpty()) {
            emailParent.setError("Email is required");
            emailParent.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            passwordParent.setError("Password is required");
            passwordParent.requestFocus();
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
                if (email.equals("parent@example.com") && password.equals("parent123")) {
                    Toast.makeText(ParentLoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                    // Navigate to ParentDashboardActivity
                    Intent intent = new Intent(ParentLoginActivity.this, ParentDashboard.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ParentLoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        }, 2000); // Simulate network delay
    }
}
