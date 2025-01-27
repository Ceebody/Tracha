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

public class ParentLoginActivity extends AppCompatActivity {

    private EditText emailParent, passwordParent;
    private Button loginButton;
    private ProgressBar progressBar;
    private TextView signUpText, forgotPasswordText;
    private FirebaseAuth mAuth;

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
        forgotPasswordText = findViewById(R.id.forgot_password);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Set login button click listener
        loginButton.setOnClickListener(v -> loginParent());

        // Set sign-up text click listener
        signUpText.setOnClickListener(v -> {
            // Navigate to Parent Registration Activity
            Intent intent = new Intent(ParentLoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Set forgot password text click listener
        forgotPasswordText.setOnClickListener(v -> {
            Intent intent = new Intent(ParentLoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
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

        // Show progress bar while authenticating
        progressBar.setVisibility(View.VISIBLE);

        // Firebase Authentication
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(ParentLoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                        // Navigate to ParentDashboardActivity
                        Intent intent = new Intent(ParentLoginActivity.this, ParentDashboard.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ParentLoginActivity.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
