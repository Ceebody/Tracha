package com.example.trachax;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OtpActivity extends AppCompatActivity {

    private EditText childNameEditText, phoneEditText;
    private Button sendTokenButton;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference tokensRef;
    private FirebaseAuth firebaseAuth;

    private static final int SMS_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        // Initialize UI elements
        childNameEditText = findViewById(R.id.child_name);
        phoneEditText = findViewById(R.id.phone);
        sendTokenButton = findViewById(R.id.send_token);

        // Initialize Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        tokensRef = firebaseDatabase.getReference("tokens");

        // Handle button click
        sendTokenButton.setOnClickListener(v -> {
            String childName = childNameEditText.getText().toString().trim();
            String phoneNumber = phoneEditText.getText().toString().trim();

            if (childName.isEmpty() || phoneNumber.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check SMS permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
            } else {
                processToken(childName, phoneNumber);
            }
        });
    }

    private void processToken(String childName, String phoneNumber) {
        // Generate a random token
        String token = generateToken();

        // Send the token via SMS
        sendTokenToDriver(token, phoneNumber);

        // Save the token to Firebase under the parent's UID
        String parentId = firebaseAuth.getCurrentUser() != null ? firebaseAuth.getCurrentUser().getUid() : null;
        if (parentId != null) {
            saveTokenToFirebase(parentId, token, childName, phoneNumber);
        } else {
            Toast.makeText(this, "Parent ID not found. Please log in again.", Toast.LENGTH_SHORT).show();
        }

        // Transition to ConfirmActivity and pass the token
        Intent intent = new Intent(this, GeneratedTokenActivity.class);
        intent.putExtra("generated_token", token);
        startActivity(intent);
        finish(); // Close the current activity
    }

    private String generateToken() {
        int randomNumber = (int) (Math.random() * 9000) + 1000; // Generate a 4-digit number
        return "YBS2919" + randomNumber; // Append it to a prefix
    }

    private void sendTokenToDriver(String token, String phoneNumber) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            String message = "Your verification token is: " + token + ". Please go to confirm activity to proceed.";
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this, "Token sent to driver!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send token. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveTokenToFirebase(String parentId, String token, String childName, String phoneNumber) {
        Token tokenData = new Token(token, childName, phoneNumber);
        tokensRef.child(parentId).child("tokens").push().setValue(tokenData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Token saved to Firebase", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to save token", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Retry processing the token after permission is granted
                String childName = childNameEditText.getText().toString().trim();
                String phoneNumber = phoneEditText.getText().toString().trim();
                processToken(childName, phoneNumber);
            } else {
                Toast.makeText(this, "SMS permission is required to send the token.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static class Token {
        public String token;
        public String childName;
        public String phoneNumber;

        // Default constructor required for calls to DataSnapshot.getValue(Token.class)
        public Token() {
        }

        public Token(String token, String childName, String phoneNumber) {
            this.token = token;
            this.childName = childName;
            this.phoneNumber = phoneNumber;
        }
    }
}
