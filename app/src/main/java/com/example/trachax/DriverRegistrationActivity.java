package com.example.trachax;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DriverRegistrationActivity extends AppCompatActivity {

    private ListView driversListView;
    private DatabaseReference databaseReference;
    private DriverAdapter adapter;
    private List<User> driversList;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_registration);

        // Initialize UI components
        driversListView = findViewById(R.id.driversListView);
        progressBar = findViewById(R.id.progressBar);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Initialize driver list
        driversList = new ArrayList<>();

        // Load drivers from Firebase and display them
        loadDrivers();

        // Handle driver click
        driversListView.setOnItemClickListener((adapterView, view, position, id) -> {
            User selectedDriver = driversList.get(position);
            showDriverMenu(view, selectedDriver);
        });
    }

    private void loadDrivers() {
        progressBar.setVisibility(View.VISIBLE); // Show progress bar while loading
        // Fetch users with the role "driver" from Firebase Realtime Database
        databaseReference.orderByChild("role").equalTo("driver").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                driversList.clear();

                // Loop through the users and get the ones with the "driver" role
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User driver = snapshot.getValue(User.class);
                    if (driver != null) {
                        driversList.add(driver);
                    }
                }

                // Set the driver list to the ListView using the custom adapter
                adapter = new DriverAdapter(DriverRegistrationActivity.this, driversList);
                driversListView.setAdapter(adapter);

                // Hide the progress bar after the data has been loaded
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database read failure
                Toast.makeText(DriverRegistrationActivity.this, "Failed to load drivers.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE); // Hide progress bar on error
            }
        });
    }

    private void showDriverMenu(View anchor, User driver) {
        PopupMenu popupMenu = new PopupMenu(this, anchor);
        popupMenu.getMenuInflater().inflate(R.menu.driver_menu2, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.action_profile:
                    // Handle profile view
                    ProfileFragment profileFragment = new ProfileFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, profileFragment)
                            .addToBackStack(null)
                            .commit();
                    Toast.makeText(DriverRegistrationActivity.this, "Opening profile of " + driver.getName(), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.action_history:
                    // Handle history view
                    Toast.makeText(this, "Viewing history of " + driver.getName(), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.action_delete:
                    // Handle driver deletion
                    deleteDriver(driver);
                    break;
            }
            return true;
        });

        popupMenu.show();
    }

    private void deleteDriver(User driver) {
        databaseReference.child(driver.getId()).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Driver deleted", Toast.LENGTH_SHORT).show();
                    driversList.remove(driver);
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to delete driver", Toast.LENGTH_SHORT).show());
    }
}
