package com.example.trachax;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

public class DriverDashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_driver_dashboard);

        // Initialize UI components
        navigationView = findViewById(R.id.driver_nav);
        drawerLayout = findViewById(R.id.main);
        toolbar = findViewById(R.id.toolbar);

        // Set up toolbar as action bar
        setSupportActionBar(toolbar);

        // Set navigation item listener
        navigationView.setNavigationItemSelectedListener(this);

        // Initialize drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Load HomeFragment initially
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
            navigationView.setCheckedItem(R.id.home); // Set default checked item
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                openFragment(new HomeFragment());
                break;

            case R.id.reg:
                openFragment(new DriverRegistrationFragment()); // Assuming this is the registration fragment for drivers
                break;

            case R.id.maps:
                openFragment(new MapsFragment());
                break;

            case R.id.otp:
                openFragment(new VerifyOtpFragment());
                break;

            case R.id.logout:
                handleLogout();
                break;

            case R.id.TnC:
                openFragment(new TermsNConditionsFragment());
                break;

            default:
                Toast.makeText(this, "Feature not implemented", Toast.LENGTH_SHORT).show();
        }

        // Close the drawer after an item is selected
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openFragment(Fragment fragment) {
        // Open the selected fragment and add to back stack for better navigation
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null) // Allows going back to previous fragment
                .commit();
    }

    private void handleLogout() {
        // Clear any session data if needed
        // Redirect to login activity
        Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(DriverDashboardActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Finish the dashboard activity to prevent going back
    }

    @Override
    public void onBackPressed() {
        // If drawer is open, close it on back press, else perform normal back operation
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
