package com.example.trachax;

import android.content.Intent;
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
        cardHome = findViewById(R.id.card_parent);
        cardVerifyOtp = findViewById(R.id.driver);
        cardMap = findViewById(R.id.activity);
        cardDeleteUser = findViewById(R.id.delete);
        cardTerms = findViewById(R.id.bus);

        // Set initial data
        showFullname.setText("Admin Name"); // Set admin name

        // Set click listeners for cards
        cardHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open RegisteredParentActivity
                Intent intent = new Intent(AdminDashboardActivity.this, RegisteredParentActivity.class);
                startActivity(intent);
            }
        });

        cardVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open RegisterFragment (within an activity)
                Intent intent = new Intent(AdminDashboardActivity.this, RegisterFragmentHostActivity.class); // Ensure the fragment is hosted in an activity
                startActivity(intent);
            }
        });

        cardMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open OngoingActivity
                Intent intent = new Intent(AdminDashboardActivity.this, BusActivity.class);
                startActivity(intent);
            }
        });

        cardDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open DeleteActivity
                Intent intent = new Intent(AdminDashboardActivity.this, DeleteActivity.class);
                startActivity(intent);
            }
        });

        cardTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open BusActivity
                Intent intent = new Intent(AdminDashboardActivity.this, BusActivity.class);
                startActivity(intent);
            }
        });
    }
}
