package com.example.trachax;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class DriverDashboardActivity extends AppCompatActivity {

    private CardView cardHome, cardVerifyOTP, cardMaps, cardConfirmActivity, cardContact, cardTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_dashboard);

        // Initialize the CardViews
        cardHome = findViewById(R.id.card_home);
        cardVerifyOTP = findViewById(R.id.veryfy_otp);
        cardMaps = findViewById(R.id.card_map);
        cardConfirmActivity = findViewById(R.id.confirm_activity);
        cardContact = findViewById(R.id.card_contact);
        cardTerms = findViewById(R.id.card_terms);

        // Set onClick listeners for each card
        cardHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event for Home card
                Toast.makeText(DriverDashboardActivity.this, "Home clicked", Toast.LENGTH_SHORT).show();
                // You can start a new activity here, for example:
                // startActivity(new Intent(DriverDashboardActivity.this, HomeActivity.class));
            }
        });

        cardVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event for Verify OTP card
                Toast.makeText(DriverDashboardActivity.this, "Verify OTP clicked", Toast.LENGTH_SHORT).show();
                // startActivity(new Intent(DriverDashboardActivity.this, VerifyOTPActivity.class));
            }
        });

        cardMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event for Maps card
                Toast.makeText(DriverDashboardActivity.this, "Maps and Locations clicked", Toast.LENGTH_SHORT).show();
                // startActivity(new Intent(DriverDashboardActivity.this, MapsActivity.class));
            }
        });

        cardConfirmActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event for Confirm Activity card
                Toast.makeText(DriverDashboardActivity.this, "Confirm Activity clicked", Toast.LENGTH_SHORT).show();
                // startActivity(new Intent(DriverDashboardActivity.this, ConfirmActivity.class));
            }
        });

        cardContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event for Contact card
                Toast.makeText(DriverDashboardActivity.this, "Contact clicked", Toast.LENGTH_SHORT).show();
                // startActivity(new Intent(DriverDashboardActivity.this, ContactActivity.class));
            }
        });

        cardTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event for Terms and Conditions card
                Toast.makeText(DriverDashboardActivity.this, "Terms and Conditions clicked", Toast.LENGTH_SHORT).show();
                // startActivity(new Intent(DriverDashboardActivity.this, TermsActivity.class));
            }
        });
    }
}
