package com.example.trachax;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText fullName, idNumber, contactNumber, email, password, confirmPassword;
    private ToggleButton passwordToggle;
    private Button registerButton;
    private TextView loginLink, termsLink;
    private RadioGroup roleRadioGroup;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

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
        progressBar = findViewById(R.id.progressBar);

        // Password visibility toggle
        passwordToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int inputType = isChecked
                    ? InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
            password.setInputType(inputType);
            confirmPassword.setInputType(inputType);
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
        String name = fullName.getText().toString().trim();
        String id = idNumber.getText().toString().trim();
        String contact = contactNumber.getText().toString().trim();
        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();
        String userConfirmPassword = confirmPassword.getText().toString().trim();
        int selectedRoleId = roleRadioGroup.getCheckedRadioButtonId();

        // Validate inputs
        if (name.isEmpty() || id.isEmpty() || contact.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty() || userConfirmPassword.isEmpty() || selectedRoleId == -1) {
            Toast.makeText(this, "Please fill all fields and select a role.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidEmail(userEmail)) {
            Toast.makeText(this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidPhoneNumber(contact)) {
            Toast.makeText(this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!userPassword.equals(userConfirmPassword)) {
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get selected role
        RadioButton selectedRoleButton = findViewById(selectedRoleId);
        String role = selectedRoleButton.getText().toString().toLowerCase();

        // Show progress bar
        progressBar.setVisibility(View.VISIBLE);
        registerButton.setEnabled(false);

        // Register user with Firebase Authentication
        mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(task -> {
                    // Hide progress bar
                    progressBar.setVisibility(View.GONE);
                    registerButton.setEnabled(true);

                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();

                            // Save user details to Firebase Database
                            HashMap<String, String> userDetails = new HashMap<>();
                            userDetails.put("name", name);
                            userDetails.put("id", id);
                            userDetails.put("contact", contact);
                            userDetails.put("email", userEmail);
                            userDetails.put("role", role);

                            databaseReference.child(userId).setValue(userDetails).addOnCompleteListener(dbTask -> {
                                if (dbTask.isSuccessful()) {
                                    saveUserDetails(name, role);
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(name)
                                            .build();
                                    firebaseUser.updateProfile(profileUpdates)
                                            .addOnCompleteListener(profileTask -> {
                                                if (profileTask.isSuccessful()) {
                                                    Toast.makeText(RegisterActivity.this, "Registration successful! Redirecting...", Toast.LENGTH_SHORT).show();
                                                    Intent intent = role.equals("driver")
                                                            ? new Intent(this, DriverHomeActivity.class)
                                                            : new Intent(this, ParentDashboard.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Failed to save user details. Try again.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error occurred";
                        Toast.makeText(RegisterActivity.this, "Registration failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserDetails(String fullName, String role) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("full_name", fullName); // Save full name
        editor.putString("user_role", role);    // Save user role
        editor.apply();
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("\\d{9}"); // 9-digit phone number
    }
}
