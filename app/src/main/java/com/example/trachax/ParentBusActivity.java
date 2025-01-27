package com.example.trachax;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONObject;

public class ParentBusActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker busMarker;
    private DatabaseReference busRef;
    private TextView etaText;

    private final LatLng parentLocation = new LatLng(5.6300, -0.1800); // Replace with actual parent's location

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_bus);

        etaText = findViewById(R.id.eta_text);

        // Initialize Firebase reference
        busRef = FirebaseDatabase.getInstance().getReference("busLocation");

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

        // Default view: parent's location
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(parentLocation, 15f));

        fetchBusLocation();
    }

    private void fetchBusLocation() {
        busRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    double latitude = snapshot.child("latitude").getValue(Double.class);
                    double longitude = snapshot.child("longitude").getValue(Double.class);

                    if (latitude != 0 && longitude != 0) {
                        LatLng busLocation = new LatLng(latitude, longitude);
                        updateBusLocation(busLocation);
                        calculateETA(busLocation);
                    }
                } else {
                    Toast.makeText(ParentBusActivity.this, "Bus location not available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Failed to fetch bus location: " + error.getMessage());
            }
        });
    }

    private void updateBusLocation(LatLng latLng) {
        if (busMarker == null) {
            busMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Bus Location")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.school_bus))); // Replace with your drawable
        } else {
            busMarker.setPosition(latLng);
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
    }

    private void calculateETA(LatLng busLocation) {
        String apiKey = "AIzaSyCY9oturc9bql33PSUkfNUptcHXla2RWN4";
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="
                + busLocation.latitude + "," + busLocation.longitude
                + "&destinations=" + parentLocation.latitude + "," + parentLocation.longitude
                + "&key=" + apiKey;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseBody);
                    JSONArray rows = jsonObject.getJSONArray("rows");
                    JSONObject elements = rows.getJSONObject(0).getJSONArray("elements").getJSONObject(0);
                    String duration = elements.getJSONObject("duration").getString("text");

                    runOnUiThread(() -> etaText.setText("ETA: " + duration));
                }
            } catch (Exception e) {
                Log.e("ETA", "Failed to calculate ETA: " + e.getMessage());
            }
        }).start();
    }
}
