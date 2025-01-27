package com.example.trachax;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ConfirmActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final LatLng DEFAULT_SCHOOL_LOCATION = new LatLng(5.714828206618381, -0.12999161912267665); // School Location
    private static final String REQUIRED_TOKEN = "YBS2919"; // Predefined token for drivers
    private static final String DIRECTIONS_API_KEY = "AIzaSyCY9oturc9bql33PSUkfNUptcHXla2RWN4";

    private GoogleMap mMap;
    private Marker parentHouseMarker, driverMarker;
    private LatLng parentLocation;
    private LatLng driverLocation;

    private TextView childNameTextView;
    private DatabaseReference databaseReference;
    private String parentId, parentName, childName;
    private Polyline routePolyline;

    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        // Bind Views
        childNameTextView = findViewById(R.id.child_name_display);
        AppCompatButton pickupButton = findViewById(R.id.button1);
        AppCompatButton dropOffButton = findViewById(R.id.button2);
        AppCompatButton contactButton = findViewById(R.id.contact);

        // Initialize FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference("parents");

        // Initialize Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Token Verification Dialog
        verifyDriverToken();

        // Button Listeners
        pickupButton.setOnClickListener(v -> handlePickup());
        dropOffButton.setOnClickListener(v -> handleDropOff());
        contactButton.setOnClickListener(v -> navigateToContact());
    }

    private void verifyDriverToken() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Token Verification");
        builder.setMessage("Please enter the verification token sent to your phone:");

        final EditText tokenInput = new EditText(this);
        tokenInput.setHint("Enter Token");
        builder.setView(tokenInput);

        builder.setPositiveButton("Confirm", (dialog, which) -> {
            String enteredToken = tokenInput.getText().toString().trim();
            if (enteredToken.isEmpty()) {
                Toast.makeText(this, "Token cannot be empty. Please try again.", Toast.LENGTH_SHORT).show();
                verifyDriverToken();
            } else if (enteredToken.equals(REQUIRED_TOKEN)) {
                Toast.makeText(this, "Token verified. Access granted.", Toast.LENGTH_SHORT).show();
                fetchParentDetails();
            } else {
                Toast.makeText(this, "Invalid token. Please try again.", Toast.LENGTH_SHORT).show();
                verifyDriverToken();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            Toast.makeText(this, "Access denied.", Toast.LENGTH_SHORT).show();
            finish();
        });

        builder.setCancelable(false);
        builder.show();
    }

    private void fetchParentDetails() {
        String childNameForLookup = "John Doe"; // Example child's name for lookup
        databaseReference.orderByChild("childName").equalTo(childNameForLookup)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot parentSnapshot : dataSnapshot.getChildren()) {
                                parentId = parentSnapshot.getKey();
                                parentName = parentSnapshot.child("name").getValue(String.class);
                                childName = parentSnapshot.child("childName").getValue(String.class);
                                double lat = parentSnapshot.child("location").child("latitude").getValue(Double.class);
                                double lng = parentSnapshot.child("location").child("longitude").getValue(Double.class);
                                parentLocation = new LatLng(lat, lng);
                                childNameTextView.setText("Child: " + childName);
                                Toast.makeText(ConfirmActivity.this, "Parent found: " + parentName, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ConfirmActivity.this, "Parent not found.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(ConfirmActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void handlePickup() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            return;
        }

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        driverLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        if (driverMarker != null) driverMarker.remove();

                        // Add driver marker with car icon
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.car_bitmap);
                        Bitmap smallMarker = Bitmap.createScaledBitmap(bitmapDrawable.getBitmap(), 80, 80, false);
                        driverMarker = mMap.addMarker(new MarkerOptions()
                                .position(driverLocation)
                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                .title("Driver Location"));

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(driverLocation, 14));

                        if (parentLocation != null) {
                            drawRoute(driverLocation, parentLocation);
                        }
                    } else {
                        Toast.makeText(this, "Unable to fetch driver location.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void handleDropOff() {
        if (parentId != null) {
            String messageToParent = "Your child " + childName + " has been dropped off successfully.";
            String messageToAdmin = childName + " has been dropped off successfully.";
            Toast.makeText(this, "Message to Parent: " + messageToParent, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Message to Admin: " + messageToAdmin, Toast.LENGTH_SHORT).show();
            showRatingDialog();
        } else {
            Toast.makeText(this, "Parent details not found. Please verify the token.", Toast.LENGTH_SHORT).show();
        }
    }

    private void drawRoute(LatLng origin, LatLng destination) {
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="
                + origin.latitude + "," + origin.longitude
                + "&destination=" + destination.latitude + "," + destination.longitude
                + "&key=" + DIRECTIONS_API_KEY;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(ConfirmActivity.this, "Failed to fetch route", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONArray routes = jsonObject.getJSONArray("routes");
                        if (routes.length() > 0) {
                            JSONArray steps = routes.getJSONObject(0).getJSONArray("legs")
                                    .getJSONObject(0).getJSONArray("steps");
                            List<LatLng> path = new ArrayList<>();
                            for (int i = 0; i < steps.length(); i++) {
                                JSONObject step = steps.getJSONObject(i);
                                JSONObject start = step.getJSONObject("start_location");
                                JSONObject end = step.getJSONObject("end_location");
                                path.add(new LatLng(start.getDouble("lat"), start.getDouble("lng")));
                                path.add(new LatLng(end.getDouble("lat"), end.getDouble("lng")));
                            }

                            runOnUiThread(() -> {
                                if (routePolyline != null) {
                                    routePolyline.remove();
                                }
                                routePolyline = mMap.addPolyline(new PolylineOptions().addAll(path).color(0xFF2196F3).width(10));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin, 14));
                            });
                        }
                    } catch (JSONException e) {
                        runOnUiThread(() -> Toast.makeText(ConfirmActivity.this, "Error parsing route", Toast.LENGTH_SHORT).show());
                    }
                }
            }
        });
    }

    private void navigateToContact() {
        Intent contactIntent = new Intent(Intent.ACTION_DIAL);
        contactIntent.setData(Uri.parse("tel:+123456789")); // Replace with parent's phone number
        startActivity(contactIntent);
    }

    private void showRatingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Rate the Service");

        RatingBar ratingBar = new RatingBar(this);
        ratingBar.setNumStars(5);
        ratingBar.setStepSize(1);
        builder.setView(ratingBar);

        builder.setPositiveButton("Submit", (dialog, which) -> {
            float rating = ratingBar.getRating();
            Toast.makeText(this, "Thank you for your rating: " + rating, Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(DEFAULT_SCHOOL_LOCATION).title("Yeriel Bracha School"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_SCHOOL_LOCATION, 14));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            handlePickup();
        } else {
            Toast.makeText(this, "Location permission denied.", Toast.LENGTH_SHORT).show();
        }
    }
}
