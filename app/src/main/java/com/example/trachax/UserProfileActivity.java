package com.example.trachax;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {

    private TextView fullNameTextView;
    private TextView contactNumberTextView;
    private TextView roleTextView;
    private TextView idNumberTextView;
    private TextView emailTextView;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        fullNameTextView = findViewById(R.id.show_fullname);
        contactNumberTextView = findViewById(R.id.show_contact);
        roleTextView = findViewById(R.id.show_role);
        idNumberTextView = findViewById(R.id.show_IdNumber);
        emailTextView = findViewById(R.id.show_email);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Assuming the user's full name is passed as an extra
        String fullName = getIntent().getStringExtra("FULL_NAME");

        // Fetch user details based on full name from Firebase Realtime Database
        fetchUserProfile(fullName);
    }

    private void fetchUserProfile(String fullName) {
        // Query to get the user data from the Firebase Realtime Database
        databaseReference.orderByChild("fullName").equalTo(fullName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Assuming the data is stored in a "users" node with child nodes like "fullName", "contactNumber", etc.
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                User user = userSnapshot.getValue(User.class);

                                // Set the user's details to the TextViews
                                if (user != null) {
                                    fullNameTextView.setText(user.getName());
                                    contactNumberTextView.setText(user.getPhone());
                                    roleTextView.setText(user.getRole());
                                    idNumberTextView.setText(user.getIdNumber());
                                    emailTextView.setText(user.getEmail());
                                }
                            }
                        } else {
                            // Handle the case where the user is not found
                            fullNameTextView.setText("User not found");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle database error
                    }
                });
    }
}
