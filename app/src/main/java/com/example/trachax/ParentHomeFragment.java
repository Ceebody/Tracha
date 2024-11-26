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

public class ParentHomeFragment extends Fragment {
    private DrawerLayout drawerLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_parent_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize CardViews
        CardView Bus = view.findViewById(R.id.bus);
        CardView Otp = view.findViewById(R.id.otp);
        CardView history = view.findViewById(R.id.history);
        CardView terms = view.findViewById(R.id.terms);

        // Set Click Listeners for each CardView
        Otp.setOnClickListener(v -> startActivity(new Intent(requireContext(), ParentOtpActivity.class)));
        Bus.setOnClickListener(v -> startActivity(new Intent(requireContext(), BusActivity.class)));
        history.setOnClickListener(v -> startActivity(new Intent(requireContext(), HistoryActivity.class)));
        terms.setOnClickListener(v -> startActivity(new Intent(requireContext(), TermsNConditionsActivity.class)));

    }
}
