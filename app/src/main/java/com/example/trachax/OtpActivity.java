package com.example.trachax;

import static android.content.Intent.getIntent;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OtpActivity extends AppCompatActivity {
    private EditText editText_otp;
    private Button verify_button;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        editText_otp = findViewById(R.id.editText_otp);
        verify_button = findViewById(R.id.verify_button);
        verificationId = getIntent().getStringExtra("verificationId");

        verify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = editText_otp.getText().toString();
                verifyOTP(otp);
            }
        });
    }

    private void verifyOTP(String otp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // OTP verified, complete the registration process
                            Toast.makeText(OtpActivity.this, "Verification successful", Toast.LENGTH_SHORT).show();
                            // Navigate to main activity or next step
                        } else {
                            // Verification failed
                            Toast.makeText(OtpActivity.this, "Verification failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}

