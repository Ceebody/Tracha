package com.example.trachax;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ChildViewHolder> {

    private List<ChildModel> children;
    private OnChildDeleteListener listener;

    public ChildAdapter(List<ChildModel> children, OnChildDeleteListener listener) {
        this.children = children;
        this.listener = listener;
    }

    // Method to add a new child
    public void addChild(ChildModel child) {
        children.add(child);
        notifyItemInserted(children.size() - 1); // Notify that a new item has been added
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_item, parent, false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder holder, int position) {
        // Get the child model object for the current position
        ChildModel child = children.get(position);
        // Bind the child data to the views
        holder.bind(child);
    }

    @Override
    public int getItemCount() {
        return children.size(); // Return the size of the children list
    }

    // Method to remove a child from the list
    public void removeChild(String childId) {
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i).getId().equals(childId)) {
                children.remove(i);
                notifyItemRemoved(i); // Notify that an item has been removed
                break;
            }
        }
    }

    class ChildViewHolder extends RecyclerView.ViewHolder {
        TextView name, age, gender, school, grade;
        Button deleteButton;

        // Constructor to bind views
        ChildViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.child_name);
            age = itemView.findViewById(R.id.child_age);
            gender = itemView.findViewById(R.id.child_gender);
            school = itemView.findViewById(R.id.child_school);
            grade = itemView.findViewById(R.id.child_class);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }

        // Method to bind data to views
        void bind(ChildModel child) {
            name.setText("Name: " + child.getName());
            age.setText("Age: " + child.getAge());
            gender.setText("Gender: " + child.getGender());
            school.setText("School: " + child.getSchool());
            grade.setText("Grade: " + child.getGrade());

            // Set up the delete button's action
            deleteButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onChildDelete(child.getId());
                }
            });
        }
    }

    // Interface for delete listener
    public interface OnChildDeleteListener {
        void onChildDelete(String childId); // Update to String as the ID is a String in Firebase

        void onChildDelete(int childId);
    }
}
