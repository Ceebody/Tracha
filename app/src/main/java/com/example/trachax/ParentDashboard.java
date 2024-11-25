package com.example.trachax;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ParentDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_dashboard);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.home:
                    selectedFragment = new ParentHomeFragment();
                    break;
                case R.id.reg:
                    selectedFragment = new RegisterFragment(); // Ensure this is the correct fragment
                    break;
                case R.id.trackdriver:
                    selectedFragment = new TrackDriverFragment();
                    break;
                case R.id.otp:
                    selectedFragment = new OtpFragment();
                    break;
                case R.id.logout:
                    selectedFragment = new LogoutActivity(); // Ensure this fragment is defined
                    break;
            }
            if (selectedFragment != null) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment, selectedFragment);
                fragmentTransaction.commit();
            }
            return true;
        });
    }
}