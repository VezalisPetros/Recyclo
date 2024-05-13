package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private TextInputEditText emailField;
    private TextInputEditText usernameField;
    private TextInputEditText passwordField;
    private TextInputEditText passwordRetypeField;
    private ProgressBar progressBar;
    private TextView loginLink;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef=database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize UI components
        initializeUI();

        // Check if user is signed in (non-null) and update UI accordingly.
        checkUserStatus();

        // Set an onClickListener for the login link
        setupLoginLink();

        // Register the user with the email and password
        setupRegisterButton();
    }

    private void initializeUI() {
        emailField = findViewById(R.id.email);
        usernameField=findViewById(R.id.username);
        passwordField = findViewById(R.id.password);
        passwordRetypeField = findViewById(R.id.passwordRetype);
        progressBar = findViewById(R.id.progressBar);
        loginLink = findViewById(R.id.loginNow);
    }

    private void checkUserStatus() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), UserMainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void setupLoginLink() {
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setupRegisterButton() {
        Button registerButton = findViewById(R.id.btn_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                // Get the email and password from the email and password fields
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();
                String passwordRetype = passwordRetypeField.getText().toString();

                // Check if the email and password fields are empty
                if (emailField.getText().toString().isEmpty() || passwordField.getText().toString().isEmpty() || passwordRetypeField.getText().toString().isEmpty()) {
                    Toast.makeText(Register.this, "Please fill in all fields", Toast.LENGTH_LONG).show();
                    return;
                }
                // Check if the password is at least 6 characters long
                if (password.length() < 6) {
                    Toast.makeText(Register.this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Check if the password and passwordRetype fields match
                if (password.equals(passwordRetype)) {
                    String email = emailField.getText().toString();
                    // Register the user with the email and password
                    registerUser(email, password, username);
                } else {
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void registerUser(String email, String password, String username) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), UserMainActivity.class);
                            FirebaseUser currentUser = auth.getCurrentUser();
                            if(email.equals("admin@gmail.com")){
                                Admin admin=new Admin(username);
                                myRef.child("admin").child(currentUser.getUid()).setValue(admin);
                                intent = new Intent(getApplicationContext(), AdminMainActivity.class);
                            } else {
                                User user=new User(username);
                                myRef.child("user").child(currentUser.getUid()).setValue(user);
                            }
                            Toast.makeText(getApplicationContext(), "Account created successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finish();
                        } else {
                            Log.w("TAG", "Registration failed: ", task.getException());
                            Toast.makeText(getApplicationContext(), "Registration failed "+task.getException(), Toast.LENGTH_LONG).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}