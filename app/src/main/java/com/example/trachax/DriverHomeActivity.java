package com.example.trachax;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.google.android.material.navigation.NavigationView;

public class DriverHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);


        // Initialize DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.navigation_drawer);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Set up ActionBarDrawerToggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Load the default fragment if this is the first creation
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DhomeFragment()).commit();
            navigationView.setCheckedItem(R.id.dhome);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;

        // Determine which item was selected
        switch (item.getItemId()) {
            case R.id.dhome:
                selectedFragment = new DhomeFragment();
                break;

            case R.id.profile:
                selectedFragment = new ProfileFragment();
                break;

            case R.id.about_us:
                selectedFragment = new AboutUsFragment();
                break;

            case R.id.share:
                selectedFragment = new ShareFragment();
                break;

            case R.id.logout:
                Toast.makeText(this, "You are logged out", Toast.LENGTH_SHORT).show();
                // Handle logout logic here (e.g., clear user session, navigate to login screen)
                break;
        }

        // Replace the fragment if one was selected
        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        }

        // Close the navigation drawer
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        // Close the drawer if it is open, otherwise call the super method
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}