package com.example.trachax;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

public class DhomeFragment extends Fragment {

    private DrawerLayout drawerLayout;
    private DatabaseHelper dbHelper; // Your existing database helper class

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dhome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the DatabaseHelper
        dbHelper = new DatabaseHelper(requireContext());

        // Initialize CardViews
        CardView cardMap = view.findViewById(R.id.card_map);
        CardView cardConfirmActivity = view.findViewById(R.id.confirm_activity);
        CardView cardContact = view.findViewById(R.id.card_contact);
        CardView cardTerms = view.findViewById(R.id.card_terms);

        // Set Click Listeners for each CardView
        cardMap.setOnClickListener(v -> startActivity(new Intent(requireContext(), MapsActivity.class)));
        cardContact.setOnClickListener(v -> startActivity(new Intent(requireContext(), ContactActivity.class)));
        cardTerms.setOnClickListener(v -> startActivity(new Intent(requireContext(), TermsNConditionsActivity.class)));

        cardConfirmActivity.setOnClickListener(v -> showPhoneNumberDialog());
    }

    private void showPhoneNumberDialog() {
        // Create an EditText for user input
        EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        input.setHint("Enter your phone number");

        // Create the AlertDialog
        new AlertDialog.Builder(requireContext())
                .setTitle("Driver Verification")
                .setMessage("Please enter your phone number:")
                .setView(input)
                .setPositiveButton("Verify", (dialog, which) -> {
                    String phoneNumber = input.getText().toString().trim();

                    if (!phoneNumber.isEmpty()) {
                        verifyDriver(phoneNumber);
                    } else {
                        Toast.makeText(requireContext(), "Phone number cannot be empty.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void verifyDriver(String phoneNumber) {
        // Query the database to check if the phone number matches a record
        Cursor cursor = dbHelper.getDriverDetailsByPhone(phoneNumber);

        if (cursor != null && cursor.moveToFirst()) {
            // Retrieve parent and child details
            String parentName = cursor.getString(cursor.getColumnIndexOrThrow("parent_name"));
            String childName = cursor.getString(cursor.getColumnIndexOrThrow("child_name"));

            Toast.makeText(requireContext(),
                    "You have been hired by " + parentName + " to pick up " + childName + ".",
                    Toast.LENGTH_LONG).show();

            // Grant access to ConfirmActivity
            startActivity(new Intent(requireContext(), ConfirmActivity.class));
        } else {
            Toast.makeText(requireContext(), "You don't have any pending activity.", Toast.LENGTH_SHORT).show();
        }

        if (cursor != null) {
            cursor.close();
        }
    }
}
