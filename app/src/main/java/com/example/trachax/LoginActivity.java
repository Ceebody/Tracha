package com.example.trachax; // Change this to your actual package name

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private ToggleButton passwordToggle;
    private Button loginButton;
    private FirebaseAuth authProfile;
    private static final String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        editTextEmail = findViewById(R.id.editText_login_email);
        editTextPassword = findViewById(R.id.editText_login_password);
        passwordToggle = findViewById(R.id.password_toggle);
        loginButton = findViewById(R.id.login_button);


        authProfile = FirebaseAuth.getInstance();

        //Define login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textEmail = editTextEmail.getText().toString();
                String textPassword = editTextPassword.getText().toString();

                if (TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(LoginActivity.this, "Please enter email address", Toast.LENGTH_SHORT).show();
                    editTextEmail.setError("Email address required");
                    editTextEmail.requestFocus();

                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                    Toast.makeText(LoginActivity.this, "Please re-enter email address", Toast.LENGTH_SHORT).show();
                    editTextEmail.setError("Valid email address required");
                    editTextEmail.requestFocus();
                } else if (TextUtils.isEmpty(textPassword)) {
                    Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                    editTextPassword.setError("Password required");
                    editTextPassword.requestFocus();
                } else {


                    loginUser(textEmail,textPassword);
                }


            }
        });


    }

    private void loginUser(String email, String password) {
        authProfile.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "You are logged in now", Toast.LENGTH_SHORT).show();

                    //Get instance of the current user
                    FirebaseUser firebaseUser = authProfile.getCurrentUser();

                    //check if email is verified
                    if (firebaseUser.isEmailVerified()){
                        Toast.makeText(LoginActivity.this, "You are logged in now", Toast.LENGTH_SHORT).show();

                        //open user profile

                        //Start UserProfileActivity

                        startActivity(new Intent(LoginActivity.this,UserProfileActivity.class));
                        finish(); //Close LoginActivity
                    }else {
                        firebaseUser.sendEmailVerification();
                        authProfile.signOut();
                        showAlertDialogue();
                    }
                } else {

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        editTextEmail.setError("User does not exist. Kindly register");
                        editTextEmail.requestFocus();

                    }catch (FirebaseAuthInvalidCredentialsException e) {
                        editTextEmail.setError("Invalid email or password. Check credentials and try again");
                        editTextEmail.requestFocus();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(LoginActivity.this, "Something went wrong. try again!", Toast.LENGTH_SHORT).show();

                    }




                }

            }
        });
    }

    private void showAlertDialogue() {
        //Set alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Email not verified");
        builder.setMessage("Please  click continue to verify your email");

        //Open email app if user clicks continue
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  //To email app in different window
                startActivity(intent);
            }
        });
        //Create alert dialogue box
        AlertDialog alertDialog = builder.create();

        //Show dialogue
        alertDialog.show();

    }
    //Check if user is already logged in
    @Override
    protected void onStart() {
        super.onStart();
        if (authProfile.getCurrentUser() !=null) {
            Toast.makeText(LoginActivity.this, "You are already logged in!", Toast.LENGTH_SHORT).show();



        } else {
            Toast.makeText(LoginActivity.this, "You can log in!", Toast.LENGTH_SHORT).show();

        }
    }
}
