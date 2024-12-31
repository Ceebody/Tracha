package com.example.trachax;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DeleteActivity extends AppCompatActivity {

    private RecyclerView userRecyclerView;
    private DatabaseHelper dbHelper;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        dbHelper = new DatabaseHelper(this);
        userRecyclerView = findViewById(R.id.userRecyclerView);

        // Fetch users from database
        List<User> users = dbHelper.getAllUsers();

        // Setup RecyclerView
        userAdapter = new UserAdapter(users, new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(User user) {
                showDeleteConfirmationDialog(user);
            }
        });
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userRecyclerView.setAdapter(userAdapter);
    }

    private void showDeleteConfirmationDialog(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete User")
                .setMessage("Are you sure you want to delete " + user.getFullName() + "?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteUser(user.getId());
                        Toast.makeText(DeleteActivity.this, user.getFullName() + " deleted.", Toast.LENGTH_SHORT).show();
                        userAdapter.removeUser(user);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
