package com.example.trachax;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisteredParentActivity extends AppCompatActivity {

    private ListView listViewParents;
    private DatabaseReference databaseReference;
    private List<String> parentNames;
    private Map<String, String> parentIds;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_parent);

        // Initialize UI components
        listViewParents = findViewById(R.id.listViewParents);
        progressBar = findViewById(R.id.progressBar);
        parentNames = new ArrayList<>();
        parentIds = new HashMap<>();

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Load parent names from the database
        loadParentNames();

        // Handle parent item click
        listViewParents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedParentName = parentNames.get(position);
                String selectedParentId = parentIds.get(selectedParentName);
                showPopupMenu(view, selectedParentName, selectedParentId);
            }
        });
    }

    private void loadParentNames() {
        progressBar.setVisibility(View.VISIBLE); // Show progress bar while loading data

        // Fetch all parents with the "parent" role
        databaseReference.orderByChild("role").equalTo("parent").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
                String parentName = dataSnapshot.child("name").getValue(String.class);
                String parentStatus = dataSnapshot.child("status").getValue(String.class);
                String parentId = dataSnapshot.getKey();

                // Check if parent name and ID are not null
                if (parentName != null && parentId != null) {
                    String displayName = parentName;
                    // Append "(Online)" to the name if the parent is online
                    if ("online".equals(parentStatus)) {
                        displayName += " (Online)";
                    }

                    parentNames.add(displayName);
                    parentIds.put(displayName, parentId);
                }

                updateListView(); // Update the ListView after adding the parent name
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
                loadParentNames(); // Reload the list if data changes
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                loadParentNames(); // Reload the list if a parent is removed
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
                // Handle child movement if needed (e.g., sorting)
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("RegisteredParentActivity", "Database error: " + databaseError.getMessage());
                Toast.makeText(RegisteredParentActivity.this, "Failed to load parent names.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE); // Hide progress bar on error
            }
        });
    }

    private void updateListView() {
        // Create an ArrayAdapter to display the parent names in the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, parentNames);
        listViewParents.setAdapter(adapter);

        progressBar.setVisibility(View.GONE); // Hide the progress bar once data is loaded
    }

    private void showPopupMenu(View view, String parentName, String parentId) {
        // Create and show the popup menu for each parent item
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.parent_options_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.option_profile:
                    // Open ProfileFragment
                    ProfileFragment profileFragment = new ProfileFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, profileFragment)
                            .addToBackStack(null)
                            .commit();
                    Toast.makeText(RegisteredParentActivity.this, "Opening profile of " + parentName, Toast.LENGTH_SHORT).show();
                    break;

                case R.id.option_children:
                    // Open HistoryActivity
                    Intent intent = new Intent(RegisteredParentActivity.this, HistoryActivity.class);
                    intent.putExtra("parentName", parentName); // Pass any required data
                    startActivity(intent);
                    Toast.makeText(RegisteredParentActivity.this, "Opening children's history for " + parentName, Toast.LENGTH_SHORT).show();
                    break;

                case R.id.option_history:
                    Toast.makeText(RegisteredParentActivity.this, "History of " + parentName, Toast.LENGTH_SHORT).show();
                    break;

                case R.id.option_delete:
                    // Handle delete parent action
                    deleteParent(parentId);
                    break;
            }
            return true;
        });

        popupMenu.show();
    }

    private void deleteParent(String parentId) {
        // Delete the selected parent from the Firebase Realtime Database
        databaseReference.child(parentId).removeValue()
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Parent deleted successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to delete parent", Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tag the current parent as online if logged in
        tagUserStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Tag the current parent as offline when the activity is paused
        tagUserStatus("offline");
    }

    private void tagUserStatus(String status) {
        String currentUserId = "currentUserId"; // Replace with the actual user ID logic
        databaseReference.child(currentUserId).child("status").setValue(status);
    }
}
