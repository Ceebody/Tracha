package com.example.trachax;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private TextView showFullname, showContact, showEmail, showRole, showIdNumber;
    private ProgressBar progressBar;
    private ImageView profileDp;

    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize UI components
        showFullname = view.findViewById(R.id.show_fullname);
        showContact = view.findViewById(R.id.show_contact);
        showEmail = view.findViewById(R.id.show_email);
        showRole = view.findViewById(R.id.show_role);
        showIdNumber = view.findViewById(R.id.show_IdNumber);
        profileDp = view.findViewById(R.id.profile_dp);
        progressBar = view.findViewById(R.id.progressbar);

        // Initialize Firebase Auth and get current user
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        // Check if user is logged in
        if (firebaseUser != null) {
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        } else {
            Toast.makeText(getActivity(), "Something went wrong. User details are not available.", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        // Get reference to the user in the database
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails userDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (userDetails != null) {
                    // Get user information from FirebaseUser and the database
                    String fullname = firebaseUser.getDisplayName() != null ? firebaseUser.getDisplayName() : "No Name Provided";
                    String email = firebaseUser.getEmail();
                    String contact = userDetails.contactNumber;
                    String role = userDetails.role;  // Ensure this is available in your data model
                    String idNumber = userDetails.idNumber;

                    // Set the values to the TextViews
                    showFullname.setText(fullname);
                    showEmail.setText(email);
                    showContact.setText(contact);
                    showRole.setText(role);
                    showIdNumber.setText(idNumber);

                    // Hide the progress bar after loading data
                    progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getActivity(), "User data is not available.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Failed to retrieve data.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
