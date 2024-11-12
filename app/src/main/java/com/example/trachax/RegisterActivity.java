package com.example.trachax;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {

    private EditText editText_register_contact_number, editText_register_full_name, editText_register_id_number, editText_register_car_number, editText_register_password, editText_register_confirm_password;
    private Button register_button;
    private String phoneNumber;
    private RadioGroup radioGroup;
    private RadioButton radioParent, radioDriver;
    private TextView textview_login;
    private TextView textView_register_car_number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        editText_register_contact_number = findViewById(R.id.editText_register_contact_number);
        editText_register_car_number = findViewById(R.id.editText_register_car_number);
        editText_register_password = findViewById(R.id.editText_register_password);
        editText_register_confirm_password = findViewById(R.id.editText_register_confirm_password);
        editText_register_id_number = findViewById(R.id.editText_register_id_number);
        editText_register_full_name = findViewById(R.id.editText_register_full_name);
        register_button = findViewById(R.id.register_button);
        radioGroup = findViewById(R.id.radio);
        radioParent = findViewById(R.id.radio_parent);
        radioDriver = findViewById(R.id.radio_driver);
        textview_login = findViewById(R.id.textview_login);


        // Initially disable input fields until a role is selected
        toggleFields(false);
        Toast.makeText(this, "Kindly select your role", Toast.LENGTH_SHORT).show();

        // Role selection logic
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_parent) {
                toggleFields(true);
                editText_register_car_number.setVisibility(View.GONE);
                textView_register_car_number.setVisibility(View.GONE);// Hide car number for parent
            } else if (checkedId == R.id.radio_driver) {
                toggleFields(true);
                editText_register_car_number.setVisibility(View.VISIBLE);
                textView_register_car_number.setVisibility(View.VISIBLE);// Show car number for driver
            }
        });

        // Register button click listener
        register_button.setOnClickListener(v -> {
            phoneNumber = editText_register_contact_number.getText().toString();
            if (phoneNumber.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Please enter your contact number", Toast.LENGTH_SHORT).show();
            } else {
                sendOTP(phoneNumber);
            }
        });

        // Login page navigation
        textview_login.setOnClickListener(v -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));

        // Show/Hide password logic
        findViewById(R.id.password_toggle).setOnClickListener(v -> togglePasswordVisibility());
    }

    // Method to enable/disable fields
    private void toggleFields(boolean enable) {
        editText_register_contact_number.setEnabled(enable);
        editText_register_car_number.setEnabled(enable);
        editText_register_password.setEnabled(enable);
        editText_register_confirm_password.setEnabled(enable);
        register_button.setEnabled(enable);
        editText_register_id_number.setEnabled(enable);
        editText_register_full_name.setEnabled(enable);
    }

    // Toggle password visibility
    private void togglePasswordVisibility() {
        if (editText_register_password.getInputType() == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            editText_register_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            editText_register_confirm_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            editText_register_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            editText_register_confirm_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        editText_register_password.setSelection(editText_register_password.getText().length());
        editText_register_confirm_password.setSelection(editText_register_confirm_password.getText().length());
    }

    // Method to send OTP
    private void sendOTP(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential credential) {
                                // Auto-verification successful, proceed with sign-in
                                Toast.makeText(RegisterActivity.this, "Verification successful!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onVerificationFailed(FirebaseException e) {
                                Toast.makeText(RegisterActivity.this, "Verification failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                                Intent intent = new Intent(RegisterActivity.this, OtpActivity.class);
                                intent.putExtra("verificationId", verificationId);
                                startActivity(intent);
                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}
