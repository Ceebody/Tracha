package com.example.trachax;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class DriverAdapter extends ArrayAdapter<User> {

    private final Context context;
    private final List<User> drivers;

    public DriverAdapter(Context context, List<User> drivers) {
        super(context, R.layout.driver_item, drivers);
        this.context = context;
        this.drivers = drivers;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.driver_item, parent, false);
        }

        User driver = drivers.get(position);

        TextView driverName = convertView.findViewById(R.id.driverName);
        ImageView statusIndicator = convertView.findViewById(R.id.statusIndicator);

        driverName.setText(driver.getName());

        // Set status indicator (online/offline)
        if ("online".equals(driver.getStatus())) {
            statusIndicator.setImageResource(R.drawable.ic_online); // Green indicator
        } else {
            statusIndicator.setImageResource(R.drawable.ic_offline); // Gray indicator
        }

        return convertView;
    }
}
