package com.example.trachax;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText et_phone_num, et_phone_otp;
    private Button btn_send_otp, btn_login;
    private String codeId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();

        // Initialize views
        et_phone_num = findViewById(R.id.et_phone_num);
        et_phone_otp = findViewById(R.id.et_phone_otp);
        btn_send_otp = findViewById(R.id.btn_send_otp);
        btn_login = findViewById(R.id.btn_login);

        // Check if phone number was passed from RegisterActivity
        String phoneNumber = getIntent().getStringExtra("phone_number");
        if (phoneNumber != null) {
            et_phone_num.setText(phoneNumber);
            sendOtp(phoneNumber);
            btn_send_otp.setVisibility(View.GONE);
            btn_login.setVisibility(View.VISIBLE);
            et_phone_otp.setVisibility(View.VISIBLE);
        }

        // OTP Callbacks
        PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(s, token);
                codeId = s;
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                // Auto-retrieve OTP
                et_phone_otp.setText(credential.getSmsCode());
                signInWithCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(PhoneActivity.this, "Verification failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        // Send OTP button click
        btn_send_otp.setOnClickListener(v -> {
            if (et_phone_num.getText().toString().isEmpty()) {
                et_phone_num.setError("Please enter a phone number");
            } else {
                String number = "+233" + et_phone_num.getText().toString();
                sendOtp(number);
                btn_send_otp.setVisibility(View.GONE);
                btn_login.setVisibility(View.VISIBLE);
                et_phone_otp.setVisibility(View.VISIBLE);
            }
        });

        // Login button click
        btn_login.setOnClickListener(v -> {
            String otp = et_phone_otp.getText().toString();
            if (otp.isEmpty()) {
                et_phone_otp.setError("Please enter OTP");
            } else {
                verifyOtp(otp);
            }
        });
    }

    // Method to send OTP
    private void sendOtp(String number) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(number)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(callbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    // Method to verify OTP
    private void verifyOtp(String phoneOtp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeId, phoneOtp);
        signInWithCredential(credential);
    }

    // Sign in with the credential and handle success or failure
    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(PhoneActivity.this, "Verification successful!", Toast.LENGTH_SHORT).show();
                    // Redirect to main dashboard or another activity
                    Intent intent = new Intent(PhoneActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(PhoneActivity.this, "Verification failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
