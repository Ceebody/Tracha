package com.example.trachax;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class RegisteredParentActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView listViewParents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_parent);

        // Initialize the DatabaseHelper with context
        dbHelper = new DatabaseHelper(this);

        // Get the ListView from the layout
        listViewParents = findViewById(R.id.listViewParents);

        // Get the list of parent names from the database
        List<String> parents = getParentNames();

        // Create an ArrayAdapter to display the list of parents in the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, parents);
        listViewParents.setAdapter(adapter);
    }

    // Method to get the list of parent names
    private List<String> getParentNames() {
        List<String> parentNames = new ArrayList<>();

        // Get a cursor with parents data
        Cursor cursor = dbHelper.getUsersByRole("Parent");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String fullName = cursor.getString(cursor.getColumnIndexOrThrow("full_name"));
                String role = cursor.getString(cursor.getColumnIndexOrThrow("role"));


                System.out.println("ID: " + id + ", Name: " + fullName);
            } while (cursor.moveToNext());
            cursor.close(); // Don't forget to close the cursor to release resources
        }

        return parentNames;
    }
}
