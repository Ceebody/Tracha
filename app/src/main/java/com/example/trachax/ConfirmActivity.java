package com.example.trachax;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class ConfirmActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button confirmPickupButton, confirmDropOffButton;

    private LatLng driverLocation = new LatLng(12.9716, 77.5946);  // Example driver's location
    private LatLng parentLocation = new LatLng(12.9352, 77.6241);  // Example parent's location

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        // Set up the buttons
        confirmPickupButton = findViewById(R.id.button1);
        confirmDropOffButton = findViewById(R.id.button2);

        // Initialize the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Set up confirm actions
        confirmPickupButton.setOnClickListener(v -> {
            Toast.makeText(this, "Pickup Confirmed", Toast.LENGTH_SHORT).show();
            showNavigation(driverLocation, parentLocation);  // Show route from driver to parent
        });

        confirmDropOffButton.setOnClickListener(v -> {
            Toast.makeText(this, "Drop-Off Confirmed", Toast.LENGTH_SHORT).show();
            showNavigation(parentLocation, driverLocation);  // Show route from parent to driver
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Enable location services if permissions are granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        mMap.setMyLocationEnabled(true);

        // Add markers for the driver and parent locations
        mMap.addMarker(new MarkerOptions().position(driverLocation).title("Driver Location"));
        mMap.addMarker(new MarkerOptions().position(parentLocation).title("Parent Location"));

        // Move camera to the driver location initially
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(driverLocation, 12));
    }

    // Method to show navigation route between two locations
    private void showNavigation(LatLng start, LatLng end) {
        // Example of a simple polyline; you would replace this with Directions API calls for a real route
        PolylineOptions polylineOptions = new PolylineOptions()
                .add(start, end)
                .width(5)
                .color(getResources().getColor(R.color.purple_500));

        mMap.addPolyline(polylineOptions);

        // Compute the bounds of the polyline
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        boundsBuilder.include(start).include(end);
        LatLngBounds bounds = boundsBuilder.build();

        // Move the camera to fit the route
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }

    // Handle location permissions result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                Toast.makeText(this, "Permission denied, location not enabled.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
