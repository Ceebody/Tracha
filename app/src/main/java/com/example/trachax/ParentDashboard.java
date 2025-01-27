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
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.trachax.databinding.ActivityParentDashboardBinding;
import com.google.android.material.navigation.NavigationView;

public class ParentDashboard extends AppCompatActivity {

    private ActivityParentDashboardBinding binding;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate layout
        binding = ActivityParentDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize drawer layout and navigation view
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);


        // Initialize shared preferences
        sharedPreferences = getSharedPreferences("ParentPrefs", MODE_PRIVATE);

        // Set the app name in the toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Trachax");
        }

        // Retrieve the parent's name from SharedPreferences
        String parentName = sharedPreferences.getString("fullname", "Parent");

        // Set the parent's name in the navigation drawer header
        View navHeaderView = navigationView.getHeaderView(0);
        TextView navName = navHeaderView.findViewById(R.id.nav_name);
        navName.setText(parentName);

        // Optionally, set a default profile picture or retrieve it if available
        String profilePicUrl = sharedPreferences.getString("profile_pic_url", null);
        ImageView navProfilePic = navHeaderView.findViewById(R.id.nav_logo);
        if (profilePicUrl != null) {
            Glide.with(this).load(profilePicUrl).into(navProfilePic);
        } else {
            navProfilePic.setImageResource(R.drawable.person_add); // Default image
        }

        // Set up ActionBar toggle for DrawerLayout
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Handle navigation drawer item clicks
        navigationView.setNavigationItemSelectedListener(this::handleDrawerNavigation);

        // Handle bottom navigation item clicks
        binding.bottomNavigation.setOnItemSelectedListener(this::handleBottomNavigation);

        // Set default fragment
        replaceFragment(new ParentHomeFragment());
    }

    private boolean handleDrawerNavigation(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                replaceFragment(new ParentHomeFragment());
                break;

            case R.id.profile:
                replaceFragment(new ProfileFragment());
                break;

            case R.id.about_us:
                replaceFragment(new AboutUsFragment());
                break;

            case R.id.logout:
                confirmLogout();
                break;

            default:
                break;
        }
        drawerLayout.closeDrawers();
        return true;
    }

    private boolean handleBottomNavigation(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                replaceFragment(new ParentHomeFragment());
                break;

            case R.id.reg:
                replaceFragment(new RegistrationFragment());
                break;

            case R.id.trackdriver:
                replaceFragment(new RegisterFragment());
                break;

            case R.id.logout:
                confirmLogout();
                break;

            default:
                break;
        }
        return true;
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

    private void replaceFragment(Fragment fragment) {
        // Handle fragment transaction and replacement
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.commit();
    }

    private void confirmLogout() {
        // Show confirmation dialog for logout
        new AlertDialog.Builder(this)
                .setTitle("Log Out")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Clear shared preferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();  //  remove specific keys (like "parent_name")
                    editor.apply();

                    // Redirect to LoginRoles activity
                    Intent intent = new Intent(ParentDashboard.this, LoginRoles.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                    // Show a toast message
                    Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
