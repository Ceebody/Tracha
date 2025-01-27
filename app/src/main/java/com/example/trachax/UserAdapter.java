package com.example.trachax;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final List<User> users; // List of users to display
    private final OnItemClickListener listener; // Listener for handling item clicks

    // Interface for click listener
    public interface OnItemClickListener {
        void onItemClick(User user);
    }

    // Constructor for the adapter
    public UserAdapter(List<User> users, OnItemClickListener listener) {
        this.users = users;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the user item layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        // Bind the user data to the view holder
        User user = users.get(position);
        holder.bind(user, listener);
    }

    @Override
    public int getItemCount() {
        // Return the total number of items
        return users.size();
    }

    // Method to remove a user from the list and notify the adapter
    public void removeUser(User user) {
        int position = users.indexOf(user);
        if (position != -1) {
            users.remove(position);
            notifyItemRemoved(position);
        }
    }

    // ViewHolder class for individual user items
    public static class UserViewHolder extends RecyclerView.ViewHolder {

        private final TextView userNameTextView;
        private final TextView userRoleTextView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            userRoleTextView = itemView.findViewById(R.id.userRoleTextView);
        }

        // Bind user data to the views
        public void bind(User user, OnItemClickListener listener) {
            userNameTextView.setText(user.getName());
            userRoleTextView.setText(user.getEmail()); // Example: Displaying email as role
            itemView.setOnClickListener(v -> listener.onItemClick(user));
        }
    }
}
