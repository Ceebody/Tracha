package com.example.trachax;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {

    private TextView parentOverview, driverOverview;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        parentOverview = findViewById(R.id.parent_overview);
        driverOverview = findViewById(R.id.driver_overview);

        databaseHelper = new DatabaseHelper(this);

        loadParentData();
        loadDriverData();
    }

    private void loadParentData() {
        List<String> parents = databaseHelper.getUsersByRole("parent");
        if (!parents.isEmpty()) {
            parentOverview.setText("Parents:\n" + String.join("\n", parents));
        } else {
            parentOverview.setText("No parents found.");
        }
    }

    private void loadDriverData() {
        List<String> drivers = databaseHelper.getUsersByRole("driver");
        if (!drivers.isEmpty()) {
            driverOverview.setText("Drivers:\n" + String.join("\n", drivers));
        } else {
            driverOverview.setText("No drivers found.");
        }
    }
}
