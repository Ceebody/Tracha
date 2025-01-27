package com.example.trachax;

import android.annotation.SuppressLint;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap nMap;
    private LocationManager locationManager;
    private String trackingToken;  // Token to track the driver
    private DatabaseReference databaseReference; // Firebase reference to store driver's location
    private final String driverTokenKey = "driver_location"; // Key for storing the location in Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Get the tracking token passed from the previous activity
        trackingToken = getIntent().getStringExtra("tracking_token");

        // Log token for debugging
        Log.d("MapsActivity", "Tracking Token: " + trackingToken);

        // Load the SupportMapFragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment_container);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Initialize Firebase Realtime Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("drivers/" + trackingToken + "/location");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        nMap = googleMap;

        // Set default location (for example, Accra, Ghana)
        LatLng accraLocation = new LatLng(5.6037, -0.1870);
        nMap.addMarker(new MarkerOptions().position(accraLocation).title("Marker in Accra"));
        nMap.moveCamera(CameraUpdateFactory.newLatLngZoom(accraLocation, 10));

        // Request location updates
        requestLocationUpdates();

        // Listen for real-time updates of the driver's location based on the token
        listenForDriverLocation();
    }

    @SuppressLint("MissingPermission")
    private void requestLocationUpdates() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);

        String provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            locationManager.requestLocationUpdates(provider, 10000, 10, this);
        }
    }

    private void listenForDriverLocation() {
        // Listen for changes in the driver's location in Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Get the driver's location from Firebase
                    double latitude = dataSnapshot.child("latitude").getValue(Double.class);
                    double longitude = dataSnapshot.child("longitude").getValue(Double.class);

                    // Update the map with the driver's location
                    LatLng driverLocation = new LatLng(latitude, longitude);
                    nMap.addMarker(new MarkerOptions().position(driverLocation).title("Driver's Location"));
                    nMap.moveCamera(CameraUpdateFactory.newLatLngZoom(driverLocation, 15));
                } else {
                    Toast.makeText(MapsActivity.this, "Driver's location not available.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("MapsActivity", "Error: " + databaseError.getMessage());
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        if (nMap != null) {
            // Update the user's current location (parent's location)
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            nMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
            nMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
        }
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
}
