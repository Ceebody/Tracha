package com.example.trachax;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminDashboardActivity extends AppCompatActivity {

    private TextView showFullname;
    private ImageView profile2;
    private CardView cardHome, cardVerifyOtp, cardMap, cardDeleteUser, cardTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Initialize views
        showFullname = findViewById(R.id.show_fullname);
        profile2 = findViewById(R.id.profile2);
        cardHome = findViewById(R.id.card_home);
        cardVerifyOtp = findViewById(R.id.verify_otp);
        cardMap = findViewById(R.id.card_map);
        cardDeleteUser = findViewById(R.id.confirm_activity);
        cardTerms = findViewById(R.id.card_terms);

        // Set initial data
        showFullname.setText("Admin Name"); // Set admin name
        // Set the profile image using Picasso or Glide library if required

        // Set click listeners for cards
        cardHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Home card click
                Toast.makeText(AdminDashboardActivity.this, "Home clicked", Toast.LENGTH_SHORT).show();
            }
        });

        cardVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Verify OTP card click
                Toast.makeText(AdminDashboardActivity.this, "Verify OTP clicked", Toast.LENGTH_SHORT).show();
            }
        });

        cardMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Maps card click
                Toast.makeText(AdminDashboardActivity.this, "Maps clicked", Toast.LENGTH_SHORT).show();
            }
        });

        cardDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Delete User card click
                Toast.makeText(AdminDashboardActivity.this, "Delete User clicked", Toast.LENGTH_SHORT).show();
            }
        });

        cardTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Terms card click
                Toast.makeText(AdminDashboardActivity.this, "Terms clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
