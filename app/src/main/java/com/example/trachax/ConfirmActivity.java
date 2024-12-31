package com.example.trachax;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.NotificationCompat;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ConfirmActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final LatLng DEFAULT_SCHOOL_LOCATION = new LatLng(12.923256, 77.677345); // Child's School Location
    private GoogleMap mMap;
    private Marker parentHouseMarker;
    private static final String CHANNEL_ID = "ParentNotificationChannel";

    private TextView childNameTextView;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        // Initialize Database Helper
        dbHelper = new DatabaseHelper(this);

        // Bind Views
        childNameTextView = findViewById(R.id.child_name_display);
        AppCompatButton pickupButton = findViewById(R.id.button1);
        AppCompatButton dropOffButton = findViewById(R.id.button2);
        AppCompatButton contactButton = findViewById(R.id.contact);

        // Retrieve Data
        String phoneNumber = getIntent().getStringExtra("phone_number");
        if (phoneNumber != null) {
            retrieveChildData(phoneNumber);
        }

        // Contact Button Action
        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmActivity.this, ContactActivity.class);
                startActivity(intent);
            }
        });

        // Initialize Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Button Actions
        pickupButton.setOnClickListener(v -> {
            Toast.makeText(this, "Pickup confirmed!", Toast.LENGTH_SHORT).show();
            sendNotification("Pickup Confirmed", "The driver has confirmed pickup for your child.");
        });

        dropOffButton.setOnClickListener(v -> showRatingPopup());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add marker for school location
        mMap.addMarker(new MarkerOptions().position(DEFAULT_SCHOOL_LOCATION).title("Child's School"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_SCHOOL_LOCATION, 14));

        // Enable map click to set parent's house location
        mMap.setOnMapClickListener(latLng -> {
            if (parentHouseMarker != null) {
                parentHouseMarker.remove();
            }
            parentHouseMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Parent's House"));
            Toast.makeText(this, "Parent's house location set!", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Displays a popup requesting ratings after drop-off confirmation.
     */
    private void showRatingPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Rate Your Experience");
        builder.setMessage("Would you like to provide a rating for this session?");

        builder.setPositiveButton("Rate Now", (dialog, which) -> {
            Toast.makeText(this, "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
            sendNotification("Drop-Off Confirmed", "The driver has confirmed drop-off for your child.");
            finish(); // Close the app
        });

        builder.setNegativeButton("No Thanks", (dialog, which) -> {
            Toast.makeText(this, "Session ended. Notification sent to the parent.", Toast.LENGTH_SHORT).show();
            sendNotification("Drop-Off Confirmed", "The driver has confirmed drop-off for your child.");
            finish(); // Close the app
        });

        builder.setCancelable(false);
        builder.show();
    }

    /**
     * Sends a notification to the parent.
     */
    private void sendNotification(String title, String message) {
        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(1, builder.build());
        }
    }

    /**
     * Creates a notification channel for sending notifications.
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Parent Notifications";
            String description = "Channel for notifying parents";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    /**
     * Retrieves child data from the database and displays it.
     */
    private void retrieveChildData(String phoneNumber) {
        Cursor cursor = dbHelper.getDriverDetailsByPhone(phoneNumber);
        if (cursor != null && cursor.moveToFirst()) {
            String childName = cursor.getString(cursor.getColumnIndexOrThrow("child_name"));
            childNameTextView.setText("Child: " + childName);
        } else {
            Toast.makeText(this, "No data found for this phone number.", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if no data
        }

        if (cursor != null) {
            cursor.close();
        }
    }
}
