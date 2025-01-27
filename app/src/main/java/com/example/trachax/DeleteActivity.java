package com.example.trachax;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeleteActivity extends AppCompatActivity {

    private ListView listViewChildren;
    private DatabaseReference databaseReference;
    private List<String> childNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        // Initialize the ListView and the list of child names
        listViewChildren = findViewById(R.id.listViewChildren);
        childNames = new ArrayList<>();

        // Initialize Firebase Database reference to the "children" node
        databaseReference = FirebaseDatabase.getInstance().getReference("children");

        // Load child names from Firebase and display them
        loadChildNames();

        // Set up item click listener to handle selection
        listViewChildren.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parentView, View view, int position, long id) {
                // Get the selected child's name
                String selectedChild = childNames.get(position);
                Toast.makeText(DeleteActivity.this, "Selected: " + selectedChild, Toast.LENGTH_SHORT).show();
                // Handle deletion logic here (e.g., show a confirmation dialog)
            }
        });
    }

    // Method to load child names from Firebase
    private void loadChildNames() {
        // Use ValueEventListener to retrieve all data at once
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the list to avoid duplicating data
                childNames.clear();

                // Check if the dataSnapshot contains children data
                if (dataSnapshot.exists()) {
                    // Loop through each child in the snapshot
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Extract the child's name from the data
                        String childName = snapshot.child("name").getValue(String.class);

                        // If the name is not null, add it to the list
                        if (childName != null) {
                            childNames.add(childName);
                        } else {
                            Log.e("DeleteActivity", "Child name is missing.");
                        }
                    }

                    // Update ListView with the fetched data
                    updateListView();
                } else {
                    // If no children exist, notify the user
                    Toast.makeText(DeleteActivity.this, "No registered children found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors
                Log.e("DeleteActivity", "Database error: " + databaseError.getMessage());
                Toast.makeText(DeleteActivity.this, "Failed to load children.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to update the ListView with the list of child names
    private void updateListView() {
        if (!childNames.isEmpty()) {
            // Create and set the adapter to update the ListView
            ArrayAdapter<String> adapter = new ArrayAdapter<>(DeleteActivity.this,
                    android.R.layout.simple_list_item_1, childNames);
            listViewChildren.setAdapter(adapter);
        } else {
            // If no children, display a message
            Toast.makeText(DeleteActivity.this, "No children available to display.", Toast.LENGTH_SHORT).show();
        }
    }
}
