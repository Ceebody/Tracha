package com.example.trachax;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class LoginRoles extends AppCompatActivity {

    private Button adminButton;
    private Button parentButton;
    private Button driverButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_roles);

        // Initialize buttons
        adminButton = findViewById(R.id.button_login);
        parentButton = findViewById(R.id.register_button);
        driverButton = findViewById(R.id.driver_button);

        // Set click listeners
        adminButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginRoles.this, AdminLoginActivity.class);
            startActivity(intent);
        });

        parentButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginRoles.this, ParentLoginActivity.class);
            startActivity(intent);
        });

        driverButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginRoles.this, DriverLoginActivity.class);
            startActivity(intent);
        });
    }
}
