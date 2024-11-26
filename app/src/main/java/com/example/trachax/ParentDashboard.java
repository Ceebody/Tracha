package com.example.trachax;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.trachax.R;
import com.example.trachax.databinding.ActivityParentDashboardBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ParentDashboard extends AppCompatActivity  {

    ActivityParentDashboardBinding binding;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityParentDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new ParentHomeFragment());
        binding.bottomNavigation.setBackground(null);

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new ParentHomeFragment());
                    break;

                case R.id.reg:
                    replaceFragment(new RegisterFragment());
                    break;

                case R.id.trackdriver:
                    replaceFragment(new TrackDriverFragment());
                    break;

                case R.id.logout:
                    Toast.makeText(this, "You are logged out", Toast.LENGTH_SHORT).show();;
                    break;


            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.commit();

    }
}
