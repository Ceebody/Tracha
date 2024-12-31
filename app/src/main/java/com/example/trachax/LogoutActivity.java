package com.example.trachax;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class LogoutActivity extends Fragment {

    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_logout, container, false);

        Button logoutButton = view.findViewById(R.id.logout_button);

        // Initialize shared preferences
        sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        // Set logout button click listener
        logoutButton.setOnClickListener(v -> showLogoutConfirmationDialog());

        return view;
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirm Logout");
        builder.setMessage("Are you sure you want to log out?");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            logoutUser();
        });

        builder.setNegativeButton("No", (dialog, which) -> {
            dialog.dismiss(); // Close the dialog if the user selects "No"
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void logoutUser() {
        // Clear user data from shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Redirect to login activity
        Intent intent = new Intent(requireContext(), LoginRoles.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
        startActivity(intent);

        // Show a toast message
        requireActivity().runOnUiThread(() -> {
            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
        });
    }
}
