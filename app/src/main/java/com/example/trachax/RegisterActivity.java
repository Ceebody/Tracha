package com.example.trachax;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextFullName, editTextIdNumber, editTextContactNumber, editTextCarNumber,
            editTextEmail, editTextPassword, editTextConfirmPassword;
    private RadioGroup radioGroup;
    private RadioButton radioParent, radioDriver;
    private Button registerButton;
    private ToggleButton passwordToggle;
    private TextView loginTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); // Ensure this matches your layout file name

        // Initialize UI elements
        loginTextView = findViewById(R.id.textview_login); // Ensure this ID matches your XML
        registerButton = findViewById(R.id.register_button);
        editTextFullName = findViewById(R.id.editText_register_full_name);
        editTextIdNumber = findViewById(R.id.editText_register_id_number);
        editTextContactNumber = findViewById(R.id.editText_register_contact_number);
        editTextCarNumber = findViewById(R.id.editText_register_car_number);
        editTextEmail = findViewById(R.id.editText_register_email);
        editTextPassword = findViewById(R.id.editText_register_password);
        editTextConfirmPassword = findViewById(R.id.editText_register_confirm_password);
        radioGroup = findViewById(R.id.radio);
        radioParent = findViewById(R.id.radio_parent);
        radioDriver = findViewById(R.id.radio_driver);
        passwordToggle = findViewById(R.id.password_toggle);


        // Set up the click listener for the Login TextView
        loginTextView.setOnClickListener(v -> {
            // Start LoginActivity when the Login TextView is clicked
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Set up password toggle button
        passwordToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Show password
                editTextPassword.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                editTextConfirmPassword.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                // Hide password
                editTextPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                editTextConfirmPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            // Move the cursor to the end of the text
            editTextPassword.setSelection(editTextPassword.length());
            editTextConfirmPassword.setSelection(editTextConfirmPassword.length());
        });

        // Set up radio group listener to show/hide car number field
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_parent) {
                // Hide car number field if Parent is selected
                editTextCarNumber.setVisibility(View.GONE);
            } else if (checkedId == R.id.radio_driver) {
                // Show car number field if Driver is selected
                editTextCarNumber.setVisibility(View.VISIBLE);
            }
        });

        // Set up register button click listener
        registerButton.setOnClickListener(v -> {
            String fullName = editTextFullName.getText().toString().trim();
            String idNumber = editTextIdNumber.getText().toString().trim();
            String contactNumber = editTextContactNumber.getText().toString().trim();
            String carNumber = editTextCarNumber.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String confirmPassword = editTextConfirmPassword.getText().toString().trim();



            if (TextUtils.isEmpty(fullName)) {
                Toast.makeText(RegisterActivity.this, "Please enter your full name", Toast.LENGTH_SHORT).show();
                editTextFullName.setError("Full name is required");
                editTextFullName.requestFocus();

            } else if (TextUtils.isEmpty(idNumber)) {
                Toast.makeText(RegisterActivity.this, "Please enter your ID number", Toast.LENGTH_SHORT).show();
               editTextIdNumber.setError("ID number required");
               editTextIdNumber.requestFocus();

            } else if (TextUtils.isEmpty(contactNumber)) {
                Toast.makeText(RegisterActivity.this, "Please enter your contact number", Toast.LENGTH_SHORT).show();
                editTextContactNumber.setError("Contact number required");
                editTextContactNumber.requestFocus();

            } else if (TextUtils.isEmpty(email)) {
                Toast.makeText(RegisterActivity.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
                editTextEmail.setError("Email address required");
                editTextEmail.requestFocus();

            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(RegisterActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                editTextPassword.setError("password required");
                editTextPassword.requestFocus();

            } else if (TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(RegisterActivity.this, "Please confirm your password", Toast.LENGTH_SHORT).show();
                editTextConfirmPassword.setError("Password rquired");
                editTextConfirmPassword.requestFocus();

            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(RegisterActivity.this, "Password do not match", Toast.LENGTH_SHORT).show();
                editTextConfirmPassword.setError("Check password");
                editTextConfirmPassword.requestFocus();
                //clear passwords
                editTextConfirmPassword.clearComposingText();
                editTextPassword.clearComposingText();

            } else if (contactNumber.length() !=10){
                Toast.makeText(RegisterActivity.this, "Contact mobile number must be ten digits", Toast.LENGTH_SHORT).show();
                editTextContactNumber.setError("Re-enter your contact number");
                editTextContactNumber.requestFocus();
                
            } else {
                registeruser (fullName,email,idNumber,contactNumber,password);
                
            }
            

            // Validate inputs
            if (validateInput(fullName, idNumber, contactNumber, carNumber, email, password, confirmPassword)) {

                // Check which role is selected
                if (radioParent.isChecked()) {
                    // Navigate to Parent Dashboard
                    Intent intent = new Intent(RegisterActivity.this, ParentDashboardActivity.class);
                    startActivity(intent);
                } else if (radioDriver.isChecked()) {
                    // Navigate to Driver Dashboard
                    Intent intent = new Intent(RegisterActivity.this, DriverDashboardActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

     private boolean validateInput(String fullName, String idNumber, String contactNumber, String carNumber, String email, String password, String confirmPassword) {
         return false;
     }

     private void registeruser(String fullName, String email, String idNumber, String contactNumber, String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        //create user profile
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            FirebaseUser firebaseUser = auth.getCurrentUser();

                            //Display name of user.
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(fullName).build();
                            firebaseUser.updateProfile(profileChangeRequest);

                            //Enter user data into the firebase realtime database
                            ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(email,idNumber,contactNumber);
                            //Extracting user reference from Database for registered users.
                            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");

                            referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        //send verification email
                                        firebaseUser.sendEmailVerification();
                                        Toast.makeText(RegisterActivity.this, "Registration successfully. Please check your email inbox for verification", Toast.LENGTH_SHORT).show();

                                        //open user profile
                                        Intent intent = new Intent(RegisterActivity.this,UserProfileActivity.class);
                                         //To prevent users from going back to Register page after pressing back
                                        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_CLEAR_TASK
                                             | intent.FLAG_ACTIVITY_NEW_TASK);

                                         startActivity(intent);
                                         finish();
                                    }else {
                                        Toast.makeText(RegisterActivity.this, "Registration failed. Please try again", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });


                        } else{
                            try{
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e){
                                editTextPassword.setError("Your password is too weak, kindly use kindly use a mix of alphabets, numbers and special characters");
                                editTextPassword.requestFocus();

                            }catch (FirebaseAuthInvalidCredentialsException e){
                                editTextEmail.setError("Your email is invalid or already in use. Kindly re-enter");
                                editTextEmail.requestFocus();
                            } catch (FirebaseAuthUserCollisionException e){
                                editTextEmail.setError("Email already exist. Use a different email address");
                                editTextEmail.requestFocus();

                            } catch (Exception e){
                                Log.e(TAG, e.getMessage());
                                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
     }

}
