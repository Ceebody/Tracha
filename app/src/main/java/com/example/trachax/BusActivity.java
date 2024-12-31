package com.example.trachax;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class BusActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker busMarker;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

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
        fetchBusLocation();
    }

    private void fetchBusLocation() {
        // Fetch the location from the database
        double[] location = databaseHelper.getBusLocation();
        double latitude = location[0];
        double longitude = location[1];

        if (latitude != 0 && longitude != 0) {
            updateBusLocation(new LatLng(latitude, longitude));
        } else {
            Log.e("Database", "No location data found.");
        }
    }

    private void updateBusLocation(LatLng latLng) {
        if (busMarker == null) {
            // Add a marker if it doesn't exist
            busMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Bus Location"));
        } else {
            // Update the position of the marker
            busMarker.setPosition(latLng);
        }

        // Move and zoom the camera to the bus location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
    }
}
