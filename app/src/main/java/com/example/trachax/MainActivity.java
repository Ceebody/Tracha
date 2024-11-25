package com.example.trachax;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize buttons
        loginButton = findViewById(R.id.button_login);
        registerButton = findViewById(R.id.register_button);

        // Set click listeners
        loginButton.setOnClickListener(v -> openLoginRoles());
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void openLoginRoles() {
        Intent intent = new Intent(MainActivity.this, LoginRoles.class);
        startActivity(intent);
    }
}
