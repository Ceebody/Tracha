package com.example.trachax;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements ChildAdapter.OnChildDeleteListener {

    private RecyclerView recyclerView;
    private ChildAdapter adapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        initializeViews();

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Load registered children
        loadRegisteredChildren();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.children_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadRegisteredChildren() {
        db.collection("children")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        List<ChildModel> children = new ArrayList<>();
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            ChildModel child = document.toObject(ChildModel.class);
                            child.setId(document.getId()); // Set document ID as child ID
                            children.add(child);
                        }
                        adapter = new ChildAdapter(children, this); // Pass `this` since HistoryActivity implements the interface
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(this, "Error fetching children", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void onChildDelete(String childId) {
        db.collection("children").document(childId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Child deleted successfully", Toast.LENGTH_SHORT).show();
                    // Reload the list after deletion
                    loadRegisteredChildren();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error deleting child: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onChildDelete(int childId) {

    }
}
