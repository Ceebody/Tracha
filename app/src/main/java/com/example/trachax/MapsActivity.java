package com.example.trachax;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import androidx.appcompat.app.AppCompatActivity; // Import AppCompatActivity


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap nMap;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps); // Ensure this layout exists

        // Load the SupportMapFragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment_container);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        nMap = googleMap;

        // Set Accra, Ghana as the default location
        LatLng accraLocation = new LatLng(5.6037, -0.1870); // Accra, Ghana
        nMap.addMarker(new MarkerOptions().position(accraLocation).title("Marker in Accra"));
        nMap.moveCamera(CameraUpdateFactory.newLatLngZoom(accraLocation, 10));

        // Request location updates
        requestLocation();
    }

    @SuppressLint("MissingPermission")
    private void requestLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);

        String provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            locationManager.requestLocationUpdates(provider, 10000, 10, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (nMap != null) {
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            nMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
            nMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Handle provider enabled
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Handle provider disabled
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Deprecated in API 29
    }
}
