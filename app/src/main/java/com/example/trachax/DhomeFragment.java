package com.example.trachax;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DhomeFragment extends Fragment {
    private DrawerLayout drawerLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dhome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize CardViews
        CardView cardHome = view.findViewById(R.id.card_home);
        CardView cardVerifyOtp = view.findViewById(R.id.verify_otp);
        CardView cardMap = view.findViewById(R.id.card_map);
        CardView cardConfirmActivity = view.findViewById(R.id.confirm_activity);
        CardView cardContact = view.findViewById(R.id.card_contact);
        CardView cardTerms = view.findViewById(R.id.card_terms);

        // Set Click Listeners for each CardView
        cardHome.setOnClickListener(v -> startActivity(new Intent(requireContext(), DriverHomeActivity.class)));
        cardVerifyOtp.setOnClickListener(v -> startActivity(new Intent(requireContext(), VerifyOtpActivity.class)));
        cardMap.setOnClickListener(v -> startActivity(new Intent(requireContext(), MapsActivity.class)));
        cardConfirmActivity.setOnClickListener(v -> startActivity(new Intent(requireContext(), ConfirmActivity.class)));
        cardContact.setOnClickListener(v -> startActivity(new Intent(requireContext(), ContactActivity.class)));
        cardTerms.setOnClickListener(v -> startActivity(new Intent(requireContext(), TermsNConditionsActivity.class)));
    }
}
