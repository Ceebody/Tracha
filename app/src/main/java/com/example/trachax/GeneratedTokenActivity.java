package com.example.trachax;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GeneratedTokenActivity extends AppCompatActivity {

    private TextView tokenTextView;
    private Button continueButton;
    private String generatedToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated_token);

        // Initialize views
        tokenTextView = findViewById(R.id.generated_token);
        continueButton = findViewById(R.id.send_button);

        // Retrieve the generated token
        generatedToken = getIntent().getStringExtra("generated_token");
        tokenTextView.setText("Token: " + generatedToken);

        // Set up continue button click listener
        continueButton.setOnClickListener(v -> {
            // Pass token to MapsActivity
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("tracking_token", generatedToken);
            startActivity(intent);
        });
    }
}


