package com.example.trachax;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.trachax.databinding.ActivityParentDashboardBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class ParentDashboard extends AppCompatActivity {

    private ActivityParentDashboardBinding binding;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityParentDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        // Set up ActionBar toggle for DrawerLayout
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Handle Navigation Drawer item clicks
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new ParentHomeFragment());
                    break;

                case R.id.profile:
                    replaceFragment(new RegisterFragment());
                    break;

                case R.id.about_us:
                    replaceFragment(new TrackDriverFragment());
                    break;

                case R.id.share:
                    replaceFragment(new ShareFragment());
                    break;

                default:
                    break;
            }
            drawerLayout.closeDrawers();
            return true;
        });

        // Handle Bottom Navigation item clicks
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new ParentHomeFragment());
                    break;

                case R.id.reg:
                    replaceFragment(new RegisterFragment());
                    break;

                case R.id.trackdriver:
                    replaceFragment(new TrackDriverFragment());
                    break;

                case R.id.logout:
                    Toast.makeText(this, "You are logged out", Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        });

        // Set default fragment
        replaceFragment(new ParentHomeFragment());
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            // Close the drawer if it's open
            drawerLayout.closeDrawers();
        } else {
            // Show confirmation dialog to exit
            new AlertDialog.Builder(this)
                    .setTitle("Exit App")
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Exit the app
                        super.onBackPressed();
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        // Dismiss the dialog
                        dialog.dismiss();
                    })
                    .show();
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.commit();
    }
}
