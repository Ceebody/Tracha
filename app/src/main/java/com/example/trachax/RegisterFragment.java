package com.example.trachax;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RegisterFragment extends Fragment {

    private ListView driversListView;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        // Initialize UI components
        driversListView = view.findViewById(R.id.driversListView);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Load drivers from Firebase and display them
        loadDrivers();

        return view;
    }

    private void loadDrivers() {
        // Fetch users with the role "driver" from Firebase Realtime Database
        databaseReference.orderByChild("role").equalTo("driver").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Create a list to store driver names
                List<String> driverNames = new ArrayList<>();

                // Loop through the users and get the ones with the "driver" role
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User driver = snapshot.getValue(User.class);
                    if (driver != null && driver.getName() != null) {
                        driverNames.add(driver.getName());
                    } else {
                        driverNames.add("Unknown Driver"); // Default value for missing data
                    }
                }

                // Update the driver list in the ListView
                updateDriverList(driverNames);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database read failure
                Toast.makeText(requireContext(), "Failed to load drivers.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDriverList(List<String> driverNames) {
        // Set the driver list to the ListView
        if (!driverNames.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, driverNames);
            driversListView.setAdapter(adapter);
        } else {
            Toast.makeText(requireContext(), "No drivers available", Toast.LENGTH_SHORT).show();
        }
    }
}
