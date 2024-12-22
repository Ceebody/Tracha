package com.example.trachax;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class LoginRoles extends AppCompatActivity {

    private Button adminButton;
    private Button parentButton;
    private Button driverButton;
    private ImageView logo;

    private int tapCount = 0;
    private long firstTapTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_roles);

        // Initialize buttons and logo
        adminButton = findViewById(R.id.button_login);
        parentButton = findViewById(R.id.register_button);
        driverButton = findViewById(R.id.driver_button);
        logo = findViewById(R.id.logo);

        // Hide admin button to prevent visible access
        adminButton.setVisibility(View.GONE);

        // Parent Login Button
        parentButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginRoles.this, ParentLoginActivity.class);
            startActivity(intent);
        });

        // Driver Login Button
        driverButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginRoles.this, DriverLoginActivity.class);
            startActivity(intent);
        });

        // Admin Access via Tap Sequence on Logo
        logo.setOnClickListener(v -> {
            long currentTime = System.currentTimeMillis();
            if (firstTapTime == 0 || (currentTime - firstTapTime > 2000)) {
                tapCount = 0;
                firstTapTime = currentTime;
            }
            tapCount++;
            if (tapCount == 5) { // Trigger admin access after 5 taps
                openAdminLogin();
            }
        });

        // Long Press on Driver Button for Admin Password Dialog
        driverButton.setOnLongClickListener(v -> {
            showAdminPasswordDialog();
            return true;
        });
    }

    private void openAdminLogin() {
        Intent intent = new Intent(LoginRoles.this, AdminLoginActivity.class);
        startActivity(intent);
    }

    private void showAdminPasswordDialog() {
        // Create password input dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Admin Access");
        builder.setMessage("Enter admin password:");

        final android.widget.EditText input = new android.widget.EditText(this);
        input.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String password = input.getText().toString();
            if (validateAdminPassword(password)) {
                openAdminLogin();
            } else {
                Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private boolean validateAdminPassword(String password) {
        // Replace "admin123" with your secure password logic
        return "admin123".equals(password);
    }
}
