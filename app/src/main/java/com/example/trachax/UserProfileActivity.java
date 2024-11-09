package com.example.trachax;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {


    private TextView show_fullname, show_contact, show_email, show_role, show_IdNumber;

    private ProgressBar progressBar;
    private String fullname, email, contact, role, IdNumber;
    private ImageView profile_dp;
    private Toolbar toolbar;
    FirebaseAuth authProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);




        show_fullname = findViewById(R.id.show_fullname);
        show_contact = findViewById(R.id.show_contact);
        show_email = findViewById(R.id.show_email);
        show_IdNumber = findViewById(R.id.show_IdNumber);
        show_role = findViewById(R.id.show_role);
        progressBar = findViewById(R.id.progressbar);


        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();


        if (firebaseUser == null){
            Toast.makeText(UserProfileActivity.this, "Something went wrong. User details is not available",
                    Toast.LENGTH_SHORT).show();


        }else {
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        //Extract user reference from firebase
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails != null){
                    fullname = firebaseUser.getDisplayName();
                    email = firebaseUser.getEmail();
                    contact = readUserDetails.contactNumber;
                    IdNumber = readUserDetails.idNumber;


                    show_fullname.setText(fullname);
                    show_email.setText(email);
                    show_contact.setText(contact);
                    show_IdNumber.setText(IdNumber);
                    show_role.setText(role);

                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}