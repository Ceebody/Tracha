package com.example.trachax;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BusActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker busMarker;
    private DatabaseReference busRef;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private boolean isTracking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);

        // Initialize Firebase reference
        busRef = FirebaseDatabase.getInstance().getReference("busLocation");

        // Initialize map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Start Journey Button
        Button btnStartJourney = findViewById(R.id.btnTrackDriver);
        btnStartJourney.setOnClickListener(v -> {
            if (!isTracking) {
                startTracking();
                btnStartJourney.setText("Stop Journey");
            } else {
                stopTracking();
                btnStartJourney.setText("Start Journey");
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Set default location (Accra, Ghana)
        LatLng accra = new LatLng(5.6037, -0.1870);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(accra, 15f));
    }

    private void startTracking() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        isTracking = true;

        // Create location request
        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(5000) // Update location every 5 seconds
                .setFastestInterval(2000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        updateBusLocation(new LatLng(location.getLatitude(), location.getLongitude()));
                        updateFirebaseLocation(location);
                    }
                }
            }
        };

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        Toast.makeText(this, "Journey started. Location tracking enabled.", Toast.LENGTH_SHORT).show();
    }

    private void stopTracking() {
        if (locationCallback != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
        isTracking = false;
        Toast.makeText(this, "Journey stopped. Location tracking disabled.", Toast.LENGTH_SHORT).show();
    }

    private void updateBusLocation(LatLng latLng) {
        if (busMarker == null) {
            busMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Bus Location"));
        } else {
            busMarker.setPosition(latLng);
        }

        // Move and zoom the camera to the bus location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
    }

    private void updateFirebaseLocation(Location location) {
        busRef.setValue(new BusLocation(location.getLatitude(), location.getLongitude()))
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Bus location updated."))
                .addOnFailureListener(e -> Log.e("Firebase", "Failed to update location: " + e.getMessage()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTracking();
    }

    // Model class for Firebase
    public static class BusLocation {
        public double latitude;
        public double longitude;

        public BusLocation() {

        }

        public BusLocation(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
}
