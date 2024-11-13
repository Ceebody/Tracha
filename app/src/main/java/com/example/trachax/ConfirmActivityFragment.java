package com.example.trachax;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.trachax.R;

public class ConfirmActivityFragment extends AppCompatActivity {

    private AppCompatButton button1, button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_confrim_activity);

        // Initialize buttons
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);

        // Set up button click listeners
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle confirm pickup action
                handleConfirmPickup();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle confirm drop-off action
                handleConfirmDropOff();
            }
        });
    }

    // Method to handle Confirm Pickup
    private void handleConfirmPickup() {
        // For now, just show a Toast message (you can replace this with your logic)
        Toast.makeText(this, "Pickup Confirmed", Toast.LENGTH_SHORT).show();
    }

    // Method to handle Confirm Drop-Off
    private void handleConfirmDropOff() {
        // For now, just show a Toast message (you can replace this with your logic)
        Toast.makeText(this, "Drop-Off Confirmed", Toast.LENGTH_SHORT).show();
    }
}
