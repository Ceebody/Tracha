package com.example.trachax;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

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
        CardView cardConfirm = view.findViewById(R.id.confirm_activity);
        CardView cardMap = view.findViewById(R.id.card_map);
        CardView cardContact = view.findViewById(R.id.card_contact);
        CardView cardTerms = view.findViewById(R.id.card_terms);

        // Set Click Listeners for each CardView
        cardConfirm.setOnClickListener(v -> startActivity(new Intent(requireContext(), ConfirmActivity.class)));
        cardMap.setOnClickListener(v -> startActivity(new Intent(requireContext(), MapsActivity.class)));
        cardContact.setOnClickListener(v -> startActivity(new Intent(requireContext(), ContactActivity.class)));
        cardTerms.setOnClickListener(v -> startActivity(new Intent(requireContext(), TermsNConditionsActivity.class)));
    }
}
