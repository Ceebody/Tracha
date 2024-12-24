package com.example.trachax;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText fullName, idNumber, contactNumber, email, password, confirmPassword;
    private ToggleButton passwordToggle;
    private Button registerButton;
    private TextView loginLink, termsLink;
    private RadioGroup roleRadioGroup;
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
        roleRadioGroup = findViewById(R.id.radio_group);
        termsLink = findViewById(R.id.terms);

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

        // Set click listener on the Terms and Conditions TextView
        termsLink.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, TermsNConditionsActivity.class);
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
        int selectedRoleId = roleRadioGroup.getCheckedRadioButtonId();

        if (name.isEmpty() || id.isEmpty() || contact.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty() || userConfirmPassword.isEmpty() || selectedRoleId == -1) {
            Toast.makeText(this, "Please fill all fields and select a role.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!userPassword.equals(userConfirmPassword)) {
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get selected role
        RadioButton selectedRoleButton = findViewById(selectedRoleId);
        String role = selectedRoleButton.getText().toString().toLowerCase();

        // Save to DB
        boolean isInserted = dbHelper.insertUser(name, id, contact, userEmail, userPassword, role);

        if (isInserted) {
            saveUserRole(role); // Save the role for redirection
            Toast.makeText(this, "Registration successful! Redirecting...", Toast.LENGTH_SHORT).show();

            // Redirect based on role
            Intent intent = role.equals("driver") ? new Intent(this, DriverHomeActivity.class)
                    : new Intent(this, ParentDashboard.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Registration failed! Try again.", Toast.LENGTH_SHORT).show();
        }
    }

    // Save user role to SharedPreferences
    private void saveUserRole(String role) {
        SharedPreferences sharedPreferences = getSharedPreferences("TrachaxPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_role", role);
        editor.apply();
    }
}
