package com.example.trachax;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Check  ActionBar before setting its title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Tracha");
        }
        new Handler().postAtTime(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));

            }
        },3000);
    }



}