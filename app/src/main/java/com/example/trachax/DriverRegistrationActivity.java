package com.example.trachax;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DriverRegistrationActivity extends AppCompatActivity {

    private ListView driversListView;
    private DatabaseHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_registration);

        // Initialize UI components
        driversListView = findViewById(R.id.driversListView);
        dbHelper = new DatabaseHelper(this);

        // Load drivers from the database and display them
        loadDrivers();
    }

    private void loadDrivers() {
        // Query the database for users with the role "driver"
        Cursor cursor = (Cursor) dbHelper.getUsersByRole("driver");
        ArrayList<String> drivers = new ArrayList<>();

        // Check if cursor is not null and contains data
        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    // Retrieve the driver's name
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    drivers.add(name);
                } while (cursor.moveToNext());
            } catch (Exception e) {
                Toast.makeText(this, "Error loading drivers: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            } finally {
                // Close the cursor to avoid memory leaks
                cursor.close();
            }
        } else {
            // Handle the case where no drivers are found
            Toast.makeText(this, "No drivers found.", Toast.LENGTH_SHORT).show();
        }

        // Set the driver list to the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, drivers);
        driversListView.setAdapter(adapter);
    }
}
