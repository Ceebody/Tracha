package com.example.trachax;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfileActivity extends AppCompatActivity {


    private TextView show_fullname, show_cntact, show_email, show_role;

    private ProgressBar progressBar;
    private String fullname, email, contact, role;
    private ImageView profile_dp;
    FirebaseAuth authProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);

        getSupportActionBar().setTitle("Profile");
        show_fullname = findViewById(R.id.show_fullname);
        show_cntact = findViewById(R.id.show_contact);
        show_email = findViewById(R.id.show_email);
        show_role = findViewById(R.id.show_role);
        progressBar = findViewById(R.id.progressbar);


        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();


        if (firebaseUser == null){
            Toast.makeText(UserProfileActivity.this, "Something went wrong. User details is not available", Toast.LENGTH_SHORT).show();
        }
    }
}