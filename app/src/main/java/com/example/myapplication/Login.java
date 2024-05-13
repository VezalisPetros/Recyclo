package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private TextInputEditText emailField;
    private TextInputEditText passwordField;
    private ProgressBar progressBar;
    private TextView registerLink;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI components
        initializeUI();

        // Setup register link
        setupRegisterLink();

        // Setup login button
        setupLoginButton();
    }

    private void initializeUI() {
        auth = FirebaseAuth.getInstance();
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        registerLink = findViewById(R.id.registerNow);
    }

    private void setupRegisterLink() {
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setupLoginButton() {
        // Set an onClickListener for the login button
        Button loginButton = findViewById(R.id.btn_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();

                //check if email and password is empty
                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Email and Password is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                //login user
                loginUser(email, password);
            }
        });
    }

    private void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    // Hide the progress bar
                    progressBar.setVisibility(View.GONE);

                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();
                        Intent intent;
                        //check if user is admin
                        if(email.equals("admin@gmail.com") && password.equals("admin1")){
                            intent = new Intent(getApplicationContext(), AdminMainActivity.class);
                        }
                        //if not admin, then user
                        else{
                            intent = new Intent(getApplicationContext(), UserMainActivity.class);
                        }
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
                    }
                    progressBar.setVisibility(View.GONE);
                });
    }
}