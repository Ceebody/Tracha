package com.example.trachax;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterFragmentHostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_fragment_host);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new RegisterFragment())
                    .commit();
        }
    }
}
