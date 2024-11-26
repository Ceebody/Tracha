package com.example.trachax;

import static android.app.PendingIntent.getActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

public class ParentOtpActivity extends AppCompatActivity {
    EditText editText1, editText2;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_parent_otp);
        // Initialize views
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        button = findViewById(R.id.button);

        // Set a click listener for the send OTP button
        button.setOnClickListener(v -> sendOtp());


    }

    private void sendOtp() {
        // Get the mobile number and OTP text
        String mobileNumber = editText1.getText().toString().trim();
        String otp = editText2.getText().toString().trim();

        // Basic validation
        if (TextUtils.isEmpty(mobileNumber)) {
            Toast.makeText(getActivity(), "Please enter a mobile number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(otp)) {
            Toast.makeText(getActivity(), "Please enter the OTP", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the mobile number is valid (basic validation for now)
        if (mobileNumber.length() < 10) {
            Toast.makeText(getActivity(), "Please enter a valid mobile number", Toast.LENGTH_SHORT).show();
            return;
        }

        // simulate sending the OTP
        Toast.makeText(getActivity(), "OTP Sent to " + mobileNumber, Toast.LENGTH_SHORT).show();


        //NavController navController = Navigation.findNavController(view);
        // navController.navigate(R.id.action_verifyOtpFragment_to_verifyOtpCodeFragment);
    }

    private Context getActivity() {
        return null;
    }

}
