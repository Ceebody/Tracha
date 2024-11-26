package com.example.trachax;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText username;
    Button resetBtn;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

         username = findViewById(R.id.username);
         resetBtn = findViewById(R.id.reset_btn);
         dbHelper = new DatabaseHelper(this);

         resetBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String usernameText = username.getText().toString();
                 Boolean checkUsername = dbHelper.checkUsername(usernameText);
                 if(checkUsername == true) {
                     Intent intent = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
                     intent.putExtra("username", usernameText);
                     startActivity(intent);
                 } else {
                     username.setError("User does not exist");
                 }
             }
         });

    }
}