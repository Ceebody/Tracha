package com.example.trachax;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DriverHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);

        // Set up the toolbar as the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);

        // Initialize the DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.navigation_drawer);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Get Firebase Auth instance and current user
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        // Set default values for user details
        String fullName = "Driver";
        String profilePicUrl = null;

        // If a user is logged in, fetch their details
        if (currentUser != null) {
            fullName = currentUser.getDisplayName() != null ? currentUser.getDisplayName() : "Driver";
            profilePicUrl = currentUser.getPhotoUrl() != null ? currentUser.getPhotoUrl().toString() : null;
        }

        // Customize navigation drawer header
        View headerView = navigationView.getHeaderView(0);
        TextView navName = headerView.findViewById(R.id.nav_name);
        ImageView navProfilePic = headerView.findViewById(R.id.nav_logo);

        navName.setText(fullName);
        if (profilePicUrl != null) {
            Glide.with(this).load(profilePicUrl).into(navProfilePic);
        } else {
            navProfilePic.setImageResource(R.drawable.person_add); // Default image
        }

        // Set up the toggle button for the drawer layout
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Load default fragment if activity is created for the first time
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DhomeFragment()).commit();
            navigationView.setCheckedItem(R.id.dhome); // Highlight "Home" menu item
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;

        // Handle navigation item selection
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
            case R.id.logout:
                confirmLogout();
                break;
        }

        // Load the selected fragment
        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();
        }

        drawerLayout.closeDrawer(GravityCompat.START); // Close drawer after selection
        return true;
    }

    private void confirmLogout() {
        new AlertDialog.Builder(this)
                .setTitle("Log Out")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Perform logout
                    FirebaseAuth.getInstance().signOut();

                    // Clear shared preferences
                    SharedPreferences sharedPreferences = getSharedPreferences("TrachaxPrefs", MODE_PRIVATE);
                    sharedPreferences.edit().clear().apply();

                    // Redirect to login screen
                    Intent intent = new Intent(DriverHomeActivity.this, LoginRoles.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                    Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            // Close the drawer if it's open
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            // Show the exit confirmation dialog
            new AlertDialog.Builder(this)
                    .setTitle("Exit App")
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", (dialog, which) -> finish())
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        }
    }

}
