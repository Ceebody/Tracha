package com.example.trachax;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AdminBusActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference driversRef;
    private Map<String, Marker> driverMarkers = new HashMap<>();
    private ProgressBar progressBar;

    private final LatLng DEFAULT_LOCATION = new LatLng(5.714806855594727, -0.13003453476953197); // Your desired location

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_bus);

        // Initialize Firebase reference
        driversRef = FirebaseDatabase.getInstance().getReference("drivers");

        // Initialize ProgressBar
        progressBar = findViewById(R.id.progressBar);

        // Initialize the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Center the map around the specified location
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 14f)); // Zoom level 14

        // Start fetching drivers
        fetchOnlineDrivers();
    }

    private void fetchOnlineDrivers() {
        // Show the progress bar
        progressBar.setVisibility(View.VISIBLE);

        driversRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot driverSnapshot : snapshot.getChildren()) {
                    String driverId = driverSnapshot.getKey();
                    Boolean isOnline = driverSnapshot.child("isOnline").getValue(Boolean.class);
                    if (isOnline != null && isOnline) {
                        Double latitude = driverSnapshot.child("location").child("latitude").getValue(Double.class);
                        Double longitude = driverSnapshot.child("location").child("longitude").getValue(Double.class);
                        String driverName = driverSnapshot.child("name").getValue(String.class);
                        String childPicked = driverSnapshot.child("childPicked").getValue(String.class);

                        if (latitude != null && longitude != null) {
                            LatLng driverLocation = new LatLng(latitude, longitude);
                            updateDriverMarker(driverId, driverName, childPicked, driverLocation);
                        }
                    } else {
                        removeDriverMarker(driverId);
                    }
                }

                // Hide the progress bar after loading data
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Failed to fetch drivers: " + error.getMessage());
                progressBar.setVisibility(View.GONE); // Hide progress bar on failure
            }
        });
    }

    private void updateDriverMarker(String driverId, String driverName, String childPicked, LatLng location) {
        Marker marker = driverMarkers.get(driverId);

        if (marker == null) {
            // Add new marker
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(location)
                    .title(driverName)
                    .snippet("Child: " + childPicked)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.car)); // Replace with your car icon
            marker = mMap.addMarker(markerOptions);
            driverMarkers.put(driverId, marker);
        } else {
            // Update existing marker
            marker.setPosition(location);
            marker.setTitle(driverName);
            marker.setSnippet("Child: " + childPicked);
        }

        // Animate camera movement to focus on the driver's location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 14f));
    }

    private void removeDriverMarker(String driverId) {
        Marker marker = driverMarkers.get(driverId);
        if (marker != null) {
            marker.remove();
            driverMarkers.remove(driverId);
        }
    }
}
