package com.example.trachax;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.trachax.OtpFragment;
import com.example.trachax.R;
import com.example.trachax.TrackDriverFragment;
import com.example.trachax.RegisterFragment; // Ensure this import is present
import com.example.trachax.HomeFragment; // Ensure this import is present
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ParentDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_dashboard); // Ensure this matches your layout file name

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.reg:
                        selectedFragment = new Fragment();
                        break;
                    case R.id.trackdriver:
                        selectedFragment = new TrackDriverFragment();
                        break;
                    case R.id.otp:
                        selectedFragment = new OtpFragment();
                        break;
                    case R.id.logout:
                        selectedFragment = new LogoutFragment();
                        break;
                }
                if (selectedFragment != null) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment, selectedFragment); // Use the correct ID
                    fragmentTransaction.commit();
                }
                return true;
            }
        });
    }
}