package com.example.trachax;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class DriverRegistrationActivity extends AppCompatActivity {

    private ListView driversListView;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_registration);

        // Initialize UI components
        driversListView = findViewById(R.id.driversListView);

        // Pass context to the DatabaseHelper constructor
        dbHelper = new DatabaseHelper(this);

        // Load drivers from the database and display them
        loadDrivers();
    }

    private void loadDrivers() {
        // Get the list of users with the role "driver"
        List<User> drivers = dbHelper.getUsersByRoleAsList("driver");

        // Check if the list is not empty
        if (!drivers.isEmpty()) {
            ArrayList<String> driverNames = new ArrayList<>();
            for (User driver : drivers) {
                driverNames.add(driver.getFullName()); // Assuming User class has a getFullName() method
            }

            // Set the driver list to the ListView
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, driverNames);
            driversListView.setAdapter(adapter);
        } else {
            // Handle the case where no drivers are found
            Toast.makeText(this, "No drivers found.", Toast.LENGTH_SHORT).show();
        }
    }
}
