package com.example.trachax;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ChildViewHolder> {

    private List<ChildModel> children;

    public ChildAdapter(List<ChildModel> children) {
        this.children = children;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_item, parent, false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder holder, int position) {
        ChildModel child = children.get(position);
        holder.name.setText("Name: " + child.getName());
        holder.age.setText("Age: " + child.getAge());
        holder.gender.setText("Gender: " + child.getGender());
        holder.school.setText("School: " + child.getSchool());
        holder.childClass.setText("Class: " + child.getChildClass());
    }

    @Override
    public int getItemCount() {
        return children.size();
    }

    static class ChildViewHolder extends RecyclerView.ViewHolder {
        TextView name, age, gender, school, childClass;

        public ChildViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.child_name);
            age = itemView.findViewById(R.id.child_age);
            gender = itemView.findViewById(R.id.child_gender);
            school = itemView.findViewById(R.id.child_school);
            childClass = itemView.findViewById(R.id.child_class);
        }
    }
}
