package com.example.trachax;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

public class OtpFragment extends Fragment {

    private EditText editTextMobileNumber, editTextOtp;
    private MaterialButton sendOtpButton;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.activity_verify_otp, container, false);

        // Initialize views
        editTextMobileNumber = view.findViewById(R.id.editText1);
        editTextOtp = view.findViewById(R.id.editText2);
        sendOtpButton = view.findViewById(R.id.button);

        // Set a click listener for the send OTP button
        sendOtpButton.setOnClickListener(v -> sendOtp());

        return view;
    }

    private void sendOtp() {
        // Get the mobile number and OTP text
        String mobileNumber = editTextMobileNumber.getText().toString().trim();
        String otp = editTextOtp.getText().toString().trim();

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
}
