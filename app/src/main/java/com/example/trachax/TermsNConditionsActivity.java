package com.example.trachax;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TermsNConditionsActivity extends AppCompatActivity {

    private CheckBox acceptCheckBox;
    private Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_n_conditions);

        // Bind views
        TextView termsContent = findViewById(R.id.terms_content);
        acceptCheckBox = findViewById(R.id.accept_checkbox);
        continueButton = findViewById(R.id.btn_continue);

        // Set the terms and conditions content
        String termsText = "1. You must use this application responsibly.\n" +
                "2. Unauthorized tracking or monitoring of individuals is strictly prohibited.\n" +
                "3. Ensure you have proper consent before using the app for tracking purposes.\n" +
                "4. Do not use this app for illegal or malicious activities.\n" +
                "5. The app collects certain data for providing tracking services; by using the app, you consent to this data collection.\n" +
                "6. Tracha X and its developers are not responsible for misuse of this app.\n" +
                "7. Violation of these terms may result in suspension or termination of your account.\n" +
                "8. These terms are subject to change at any time without notice.";
        termsContent.setText(termsText);

        // Enable the continue button only when the checkbox is checked
        acceptCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            continueButton.setEnabled(isChecked);
        });

        // Handle continue button click
        continueButton.setOnClickListener(view -> {
            // Close the current activity and return to the previous one
            finish();
        });
    }
}
