package com.example.trachax;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trachax.LoginRoles;

public class RegisterActivity extends AppCompatActivity {

    private EditText fullName, idNumber, contactNumber, email, password, confirmPassword;
    private ToggleButton passwordToggle;
    private Button registerButton;
    private TextView loginLink;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize the DBHelper
        dbHelper = new DatabaseHelper(this);

        // Initialize UI components
        fullName = findViewById(R.id.editText_register_full_name);
        idNumber = findViewById(R.id.editText_register_id_number);
        contactNumber = findViewById(R.id.phone);
        email = findViewById(R.id.editText_register_email);
        password = findViewById(R.id.editText_register_password);
        confirmPassword = findViewById(R.id.editText_register_confirm_password);
        passwordToggle = findViewById(R.id.password_toggle);
        registerButton = findViewById(R.id.register_button);
        loginLink = findViewById(R.id.textview_login);

        // Password visibility toggle
        passwordToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                password.setInputType(0x00000091); // Show password
                confirmPassword.setInputType(0x00000091); // Show password
            } else {
                password.setInputType(0x00000081); // Hide password
                confirmPassword.setInputType(0x00000081); // Hide password
            }
        });

        // Handle registration logic
        registerButton.setOnClickListener(v -> registerUser());

        // Navigate to login
        loginLink.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginRoles.class);
            startActivity(intent);
        });
    }

    private void registerUser() {
        String name = fullName.getText().toString();
        String id = idNumber.getText().toString();
        String contact = contactNumber.getText().toString();
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
        String userConfirmPassword = confirmPassword.getText().toString();

        // Validate inputs
        if (name.isEmpty() || id.isEmpty() || contact.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty() || userConfirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!userPassword.equals(userConfirmPassword)) {
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save to DB
        boolean isInserted = dbHelper.insertUser(name, id, contact, userEmail, userPassword);

        if (isInserted) {
            Toast.makeText(this, "Registration successful! Please log in.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, LoginRoles.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Registration failed! Try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
