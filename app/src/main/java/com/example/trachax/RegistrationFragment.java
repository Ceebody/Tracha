package com.example.trachax;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RegistrationFragment extends Fragment {

    private EditText childName, childDOB, schoolName, childClass;
    private RadioGroup genderGroup;
    private Button registerButton;
    private RecyclerView recyclerView;
    private ChildAdapter adapter;
    private List<ChildModel> children;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        // Initialize views
        childName = view.findViewById(R.id.child_name);
        childDOB = view.findViewById(R.id.child_age);
        schoolName = view.findViewById(R.id.school_name);
        childClass = view.findViewById(R.id.child_class);
        genderGroup = view.findViewById(R.id.gender_group);
        registerButton = view.findViewById(R.id.register_button);
        recyclerView = view.findViewById(R.id.children_recycler_view);

        // Initialize RecyclerView
        children = new ArrayList<>();
        adapter = new ChildAdapter(children);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Set up register button click listener
        registerButton.setOnClickListener(v -> {
            if (validateInputs()) {
                String name = childName.getText().toString().trim();
                String dob = childDOB.getText().toString().trim();
                String school = schoolName.getText().toString().trim();
                String grade = childClass.getText().toString().trim();
                String gender = getSelectedGender();

                // Add child details to the list and update RecyclerView
                children.add(new ChildModel(name, dob, gender, school, grade));
                adapter.notifyDataSetChanged();

                // Clear input fields
                clearInputs();

                Toast.makeText(getContext(), "Child Registered", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private boolean validateInputs() {
        if (TextUtils.isEmpty(childName.getText())) {
            childName.setError("Child's name is required");
            return false;
        }
        if (TextUtils.isEmpty(childDOB.getText())) {
            childDOB.setError("Date of birth is required");
            return false;
        }
        if (genderGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getContext(), "Please select a gender", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(schoolName.getText())) {
            schoolName.setError("School name is required");
            return false;
        }
        if (TextUtils.isEmpty(childClass.getText())) {
            childClass.setError("Grade/Class is required");
            return false;
        }
        return true;
    }

    private String getSelectedGender() {
        int selectedId = genderGroup.getCheckedRadioButtonId();
        RadioButton selectedGender = genderGroup.findViewById(selectedId);
        return selectedGender.getText().toString();
    }

    private void clearInputs() {
        childName.setText("");
        childDOB.setText("");
        genderGroup.clearCheck();
        schoolName.setText("");
        childClass.setText("");
    }
}
