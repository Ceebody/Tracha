package com.example.trachax;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.hbb20.CountryCodePicker;

public class OtpActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_REQUEST_CODE = 1;

    private EditText phoneEditText;
    private Button hireDriverButton;
    private CountryCodePicker countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        // Bind views
        phoneEditText = findViewById(R.id.phone);
        hireDriverButton = findViewById(R.id.verify_button);
        countryCodePicker = findViewById(R.id.ccp);

        // Set the country code picker to work with the phone number
        countryCodePicker.registerCarrierNumberEditText(phoneEditText);

        hireDriverButton.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                // Request SMS permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
            } else {
                // Permission already granted, send SMS with deep link
                sendSmsWithDeepLink();
            }
        });
    }

    private void sendSmsWithDeepLink() {
        String phoneNumber = countryCodePicker.getFullNumberWithPlus();

        // Create the deep link
        String deepLinkUrl = "myapp://confirm";

        // Compose the message
        String message = "You have been hired as a driver. Please confirm the pickup and drop-off here: " + deepLinkUrl;

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this, "Message sent successfully!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send message. Please try again.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, send SMS
                sendSmsWithDeepLink();
            } else {
                Toast.makeText(this, "SMS permission is required to send messages.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
