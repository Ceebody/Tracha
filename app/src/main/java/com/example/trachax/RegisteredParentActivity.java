package com.example.trachax;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class RegisteredParentActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView parentsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_parent);

        dbHelper = new DatabaseHelper(this);
        parentsListView = findViewById(R.id.parentsListView);

        loadParents();
    }

    private void loadParents() {
        List<String> parents = dbHelper.getUsersByRole("parent");

        if (!parents.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, parents);
            parentsListView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No parents found.", Toast.LENGTH_SHORT).show();
        }
    }
}
