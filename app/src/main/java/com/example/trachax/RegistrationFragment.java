package com.example.trachax;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;

public class RegistrationFragment extends Fragment {

    private EditText childName, childDOB, schoolName, childClass;
    private RadioGroup genderGroup;
    private Button registerButton;
    private FirebaseFirestore db;
    private CollectionReference childrenCollection;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        initializeViews(view);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();
        childrenCollection = db.collection("children");

        registerButton.setOnClickListener(v -> {
            if (validateInputs()) {
                registerChild();
            }
        });

        return view;
    }

    private void initializeViews(View view) {
        childName = view.findViewById(R.id.child_name);
        childDOB = view.findViewById(R.id.child_age);
        schoolName = view.findViewById(R.id.school_name);
        childClass = view.findViewById(R.id.child_class);
        genderGroup = view.findViewById(R.id.gender_group);
        registerButton = view.findViewById(R.id.register_button);
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

    private void registerChild() {
        String name = childName.getText().toString().trim();
        String ageStr = childDOB.getText().toString().trim();
        String school = schoolName.getText().toString().trim();
        String grade = childClass.getText().toString().trim();
        String gender = getSelectedGender();

        // Validate age input
        int ageInt = 0;
        try {
            ageInt = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            childDOB.setError("Please enter a valid age");
            return;
        }

        ChildModel child = new ChildModel(name, ageInt, gender, school, grade);

        // Add child to Firebase Firestore
        childrenCollection.add(child)
                .addOnSuccessListener(documentReference -> {
                    child.setId(documentReference.getId()); // Set the generated document ID
                    clearInputs();
                    Toast.makeText(getContext(), "Child Registered", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("RegistrationError", "Error registering child", e);
                    Toast.makeText(getContext(), "Error registering child", Toast.LENGTH_SHORT).show();
                });
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
