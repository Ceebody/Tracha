package com.example.trachax;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class RegisterFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private ListView driversListView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        dbHelper = new DatabaseHelper(getContext());
        driversListView = view.findViewById(R.id.driversListView);

        loadDrivers();

        return view;
    }

    private void loadDrivers() {
        ArrayList<String> drivers = new ArrayList<>();
        Cursor cursor = dbHelper.getUsersByRoleCursor("driver");

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("full_name"));
                    drivers.add(name);
                }
            } catch (Exception e) {
                Toast.makeText(getContext(), "Error loading drivers: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            } finally {
                cursor.close();
            }
        } else {
            Toast.makeText(getContext(), "No drivers found.", Toast.LENGTH_SHORT).show();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, drivers);
        driversListView.setAdapter(adapter);
    }
}
