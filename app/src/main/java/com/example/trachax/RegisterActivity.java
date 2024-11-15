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

public class RegisterActivity extends AppCompatActivity {

    private EditText editText_register_contact_number, editText_register_full_name, editText_register_id_number, editText_register_car_number, editText_register_password, editText_register_confirm_password;
    private Button register_button;
    private String phoneNumber;
    private RadioGroup radioGroup;
    private RadioButton radioParent, radioDriver;
    private TextView textview_login, textView_register_car_number;

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
        textView_register_car_number = findViewById(R.id.textView_register_car_number);

        // Initially disable input fields until a role is selected
        toggleFields(false);
        Toast.makeText(this, "Please select your role", Toast.LENGTH_SHORT).show();

        // Role selection logic
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_parent) {
                toggleFields(true);
                editText_register_car_number.setVisibility(View.GONE);
                textView_register_car_number.setVisibility(View.GONE);
            } else if (checkedId == R.id.radio_driver) {
                toggleFields(true);
                editText_register_car_number.setVisibility(View.VISIBLE);
                textView_register_car_number.setVisibility(View.VISIBLE);
            }
        });

        // Register button click listener
        register_button.setOnClickListener(v -> {
            String fullName = editText_register_full_name.getText().toString().trim();
            String idNumber = editText_register_id_number.getText().toString().trim();
            String password = editText_register_password.getText().toString().trim();
            String confirmPassword = editText_register_confirm_password.getText().toString().trim();
            phoneNumber = editText_register_contact_number.getText().toString().trim();

            // Validation for all required fields
            if (fullName.isEmpty()) {
                editText_register_full_name.setError("Please enter your full name");
                editText_register_full_name.requestFocus();
                return;
            }

            if (idNumber.isEmpty()) {
                editText_register_id_number.setError("Please enter your ID number");
                editText_register_id_number.requestFocus();
                return;
            }

            if (phoneNumber.isEmpty()) {
                editText_register_contact_number.setError("Please enter your contact number");
                editText_register_contact_number.requestFocus();
                return;
            }

            if (password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Role selection check
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(RegisterActivity.this, "Please select a role", Toast.LENGTH_SHORT).show();
                return;
            }

            // Proceed to PhoneActivity for OTP verification
            Intent intent = new Intent(RegisterActivity.this, PhoneActivity.class);
            intent.putExtra("phone_number", "+233" + phoneNumber);
            startActivity(intent);
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
}
